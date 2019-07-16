package uk.ac.ncl.cemdit.controller;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

import javax.swing.text.Segment;

public class PROVN_TokenMaker extends AbstractTokenMaker {
    protected final String operators = "=|><&";
    protected final String separators = "()[]";
    protected final String separators2 = ".,";			// Characters you don't want syntax highlighted but separate identifiers.
    protected final String shellVariables = ":#-?$!*@_";	// Characters that are part of "$<char>" shell variables; e.g., "$_".
    private int currentTokenStart;
    private int currentTokenType;

    public PROVN_TokenMaker() {
        super();
    }

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap tokenMap = new TokenMap();

        tokenMap.put("default", Token.RESERVED_WORD);
        tokenMap.put("prefix", Token.RESERVED_WORD_2);
        tokenMap.put("entity", Token.OPERATOR);
        tokenMap.put("activity", Token.COMMENT_KEYWORD);
        tokenMap.put("agent", Token.IDENTIFIER);
        tokenMap.put("used", Token.FUNCTION);
        tokenMap.put("wasGeneratedBy", Token.FUNCTION);
        tokenMap.put("wasAssociatedWith", Token.FUNCTION);
        tokenMap.put("wasDerivedFrom", Token.FUNCTION);
        tokenMap.put("wasAttributedTo", Token.FUNCTION);
        tokenMap.put("wasInformedBy", Token.FUNCTION);
        tokenMap.put("actedOnBehalfOf", Token.FUNCTION);

        return tokenMap;
    }

    @Override
    public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
        switch (tokenType) {
            // Since reserved words, functions, and data types are all passed into here
            // as "identifiers," we have to see what the token really is...
            case Token.IDENTIFIER:
                int value = wordsToHighlight.get(segment, start, end);
                if (value != -1)
                    tokenType = value;
                break;
            case Token.WHITESPACE:
            case Token.SEPARATOR:
            case Token.OPERATOR:
            case Token.LITERAL_NUMBER_DECIMAL_INT:
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
            case Token.LITERAL_CHAR:
            case Token.LITERAL_BACKQUOTE:
            case Token.COMMENT_EOL:
            case Token.PREPROCESSOR:
            case Token.VARIABLE:
                break;

            default:
                new Exception("Unknown tokenType: '" + tokenType + "'").
                        printStackTrace();
                tokenType = Token.IDENTIFIER;
                break;

        }

        super.addToken(segment, start, end, tokenType, startOffset);
    }

    /**
     * Returns a list of tokens representing the given text.
     *
     * @param text           The text to break into tokens.
     * @param startTokenType The token with which to start tokenizing.
     * @param startOffset    The offset at which the line of tokens begins.
     * @return A linked list of tokens representing <code>text</code>.
     */
    public Token getTokenList(Segment text, int startTokenType, final int startOffset) {

        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int count = text.count;
        int end = offset + count;

        // See, when we find a token, its starting position is always of the form:
        // 'startOffset + (currentTokenStart-offset)'; but since startOffset and
        // offset are constant, tokens' starting positions become:
        // 'newStartOffset+currentTokenStart' for one less subraction operation.
        int newStartOffset = startOffset - offset;

        currentTokenStart = offset;
        currentTokenType  = startTokenType;
        boolean backslash = false;

//beginning:
        for (int i=offset; i<end; i++) {

            char c = array[i];

            switch (currentTokenType) {

                case Token.NULL:

                    currentTokenStart = i;	// Starting a new token here.

                    switch (c) {

                        case ' ':
                        case '\t':
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '`':
                            if (backslash) { // Escaped back quote => call '`' an identifier..
                                addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                backslash = false;
                            }
                            else {
                                currentTokenType = Token.LITERAL_BACKQUOTE;
                            }
                            break;

                        case '"':
                            if (backslash) { // Escaped double quote => call '"' an identifier..
                                addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                backslash = false;
                            }
                            else {
                                currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            }
                            break;

                        case '\'':
                            if (backslash) { // Escaped single quote => call '\'' an identifier.
                                addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                backslash = false;
                            }
                            else {
                                currentTokenType = Token.LITERAL_CHAR;
                            }
                            break;

                        case '\\':
                            addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                            backslash = !backslash;
                            break;

                        case '$':
                            if (backslash) { // Escaped dollar sign => call '$' an identifier..
                                addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                backslash = false;
                            }
                            else {
                                currentTokenType = Token.VARIABLE;
                            }
                            break;

                        case '#':
                            backslash = false;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        default:
                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            }
                            else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }
                            int indexOf = operators.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i, Token.OPERATOR, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i, Token.SEPARATOR, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators2.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            else {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }

                    } // End of switch (c).

                    break;

                case Token.WHITESPACE:

                    switch (c) {

                        case ' ':
                        case '\t':
                            break;	// Still whitespace.

                        case '\\':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            addToken(text, i,i, Token.IDENTIFIER, newStartOffset+i);
                            currentTokenType = Token.NULL;
                            backslash = true; // Previous char whitespace => this must be first backslash.
                            break;

                        case '`': // Don't need to worry about backslashes as previous char is space.
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_BACKQUOTE;
                            backslash = false;
                            break;

                        case '"': // Don't need to worry about backslashes as previous char is space.
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            backslash = false;
                            break;

                        case '\'': // Don't need to worry about backslashes as previous char is space.
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_CHAR;
                            backslash = false;
                            break;

                        case '$': // Don't need to worry about backslashes as previous char is space.
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            backslash = false;
                            break;

                        case '#':
                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.COMMENT_EOL;
                            break;

                        default:	// Add the whitespace token and start anew.

                            addToken(text, currentTokenStart,i-1, Token.WHITESPACE, newStartOffset+currentTokenStart);
                            currentTokenStart = i;

                            if (RSyntaxUtilities.isDigit(c)) {
                                currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                                break;
                            }
                            else if (RSyntaxUtilities.isLetter(c) || c=='/' || c=='_') {
                                currentTokenType = Token.IDENTIFIER;
                                break;
                            }
                            int indexOf = operators.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, i,i, Token.OPERATOR, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, i,i, Token.SEPARATOR, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators2.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, i,i, Token.IDENTIFIER, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            else {
                                currentTokenType = Token.IDENTIFIER;
                            }

                    } // End of switch (c).

                    break;

                default: // Should never happen
                case Token.IDENTIFIER:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '`': // Don't need to worry about backslashes as previous char is space.
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_BACKQUOTE;
                            backslash = false;
                            break;

                        case '"': // Don't need to worry about backslashes as previous char is non-backslash.
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            backslash = false;
                            break;

                        case '\'': // Don't need to worry about backslashes as previous char is non-backslash.
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_CHAR;
                            backslash = false;
                            break;

                        case '\\':
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            addToken(text, i,i, Token.IDENTIFIER, newStartOffset+i);
                            currentTokenType = Token.NULL;
                            backslash = true;
                            break;

                        case '$': // Don't need to worry about backslashes as previous char is non-backslash.
                            addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            backslash = false;
                            break;

                        case '=': // Special case here; when you have "identifier=<value>" in shell, "identifier" is a variable.
                            addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                            addToken(text, i,i, Token.OPERATOR, newStartOffset+i);
                            currentTokenType = Token.NULL;
                            break;

                        default:
                            if (RSyntaxUtilities.isLetterOrDigit(c) || c=='/' || c=='_') {
                                break;	// Still an identifier of some type.
                            }
                            int indexOf = operators.indexOf(c);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                addToken(text, i,i, Token.OPERATOR, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                addToken(text, i,i, Token.SEPARATOR, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators2.indexOf(c,0);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i-1, Token.IDENTIFIER, newStartOffset+currentTokenStart);
                                addToken(text, i,i, Token.IDENTIFIER, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            // Otherwise, we're still an identifier (?).

                    } // End of switch (c).

                    break;

                case Token.LITERAL_NUMBER_DECIMAL_INT:

                    switch (c) {

                        case ' ':
                        case '\t':
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.WHITESPACE;
                            break;

                        case '`': // Don't need to worry about backslashes as previous char is space.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_BACKQUOTE;
                            backslash = false;
                            break;

                        case '"': // Don't need to worry about backslashes as previous char is non-backslash.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            backslash = false;
                            break;

                        case '\'': // Don't need to worry about backslashes as previous char is non-backslash.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.LITERAL_CHAR;
                            backslash = false;
                            break;

                        case '$': // Don't need to worry about backslashes as previous char is non-backslash.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            currentTokenStart = i;
                            currentTokenType = Token.VARIABLE;
                            backslash = false;
                            break;

                        case '\\':
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            addToken(text, i,i, Token.IDENTIFIER, newStartOffset+i);
                            currentTokenType = Token.NULL;
                            backslash = true;
                            break;

                        default:

                            if (RSyntaxUtilities.isDigit(c)) {
                                break;	// Still a literal number.
                            }
                            int indexOf = operators.indexOf(c);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                                addToken(text, i,i, Token.OPERATOR, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators.indexOf(c);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                                addToken(text, i,i, Token.SEPARATOR, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }
                            indexOf = separators2.indexOf(c);
                            if (indexOf>-1) {
                                addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                                addToken(text, i,i, Token.IDENTIFIER, newStartOffset+i);
                                currentTokenType = Token.NULL;
                                break;
                            }

                            // Otherwise, remember this was a number and start over.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_NUMBER_DECIMAL_INT, newStartOffset+currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;

                    } // End of switch (c).

                    break;

                case Token.VARIABLE:

                    // Note that we first arrive here AFTER the '$' character.
                    // First check if the variable name is enclosed in '{' and '}' characters.
                    if (c=='{') {
                        while (++i<end) {
                            if (array[i]=='}') {
                                addToken(text, currentTokenStart,i, Token.VARIABLE, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                                break;
                            }
                        } // End of while (++i<end).
                        if (i==end) { // Happens when '}' wasn't found...
                            addToken(text, currentTokenStart,end-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                            currentTokenType = Token.NULL;
                        }
                        break;
                    } // End of if (i<end-1 && array[i+1]=='{').

                    // If we didn't find the '{' character, find the end of the variable...
                    while (i<end) {
                        c = array[i];	// Not needed the first iteration, but can't think of a better way to do it...
                        if (!RSyntaxUtilities.isLetterOrDigit(c) && shellVariables.indexOf(c)==-1 && c!='_') {
                            addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                            i--;
                            currentTokenType = Token.NULL;
                            break;
                        }
                        i++;
                    }

                    // This only happens if we never found the end of the variable in the loop above.
                    if (i==end) {
                        addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                        currentTokenType = Token.NULL;
                    }

                    break;

                case Token.COMMENT_EOL:
                    // If we got here, then the line != "#" only, so check for "#!".
                    if (c=='!')
                        currentTokenType = Token.PREPROCESSOR;
                    i = end - 1;
                    addToken(text, currentTokenStart,i, currentTokenType, newStartOffset+currentTokenStart);
                    // We need to set token type to null so at the bottom we don't add one more token.
                    currentTokenType = Token.NULL;

                    break;

                case Token.LITERAL_CHAR:

                    if (c=='\\') {
                        backslash = !backslash; // Okay because if we got in here, backslash was initially false.
                    }
                    else {
                        if (c=='\'' && !backslash) {
                            addToken(text, currentTokenStart,i, Token.LITERAL_CHAR, newStartOffset+currentTokenStart);
                            currentTokenStart = i + 1;
                            currentTokenType = Token.NULL;
                            // backslash is definitely false when we leave.
                        }

                        backslash = false; // Need to set backslash to false here as a character was typed.

                    }
                    // Otherwise, we're still an unclosed char literal...

                    break;

                case Token.LITERAL_BACKQUOTE:

                    switch (c) {

                        case '\\':
                            backslash = !backslash;
                            break;

                        case '`':
                            if (!backslash) {
                                addToken(text, currentTokenStart,i, Token.LITERAL_BACKQUOTE, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                                // backslash is definitely false when we leave.
                                break;
                            }
                            backslash = false;
                            break;

                        // Variable in the backquote string...
                        case '$':

                            if (backslash==true) {
                                backslash = false;
                                break;
                            }

                            // Add the string up-to the variable.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_BACKQUOTE, newStartOffset+currentTokenStart);
                            currentTokenType = Token.VARIABLE;
                            currentTokenStart = i;

                            // First check if the variable name is enclosed in '{' and '}' characters.
                            if (i<end-1 && array[i+1]=='{') {
                                i++; // Now we're on the '{' char.
                                while (++i<end) {
                                    if (array[i]=='}') {
                                        addToken(text, currentTokenStart,i, Token.VARIABLE, newStartOffset+currentTokenStart);
                                        i++;
                                        if (i<end) {
                                            c = array[i];
                                            if (c=='`') { // The only rub - back quote right after variable.
                                                addToken(text, i,i, Token.LITERAL_BACKQUOTE, newStartOffset+i);
                                                currentTokenType = Token.NULL;
                                                break;
                                            }
                                            else { // Continue on with the string.
                                                currentTokenStart = i;
                                                currentTokenType = Token.LITERAL_BACKQUOTE;
                                                i--;
                                                break;
                                            }
                                        }
                                        else { // i==end = "trick" this method so that the string is continued to the next line.
                                            currentTokenStart = i;
                                            currentTokenType = Token.LITERAL_BACKQUOTE;
                                            break; // So we don't hit the condition below.
                                        }
                                    } // End of if (array[i]=='}').
                                } // End of while (++i<end).
                                if (i==end) { // Happens when '}' wasn't found...
                                    addToken(text, currentTokenStart,end-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                                    currentTokenStart = end; // ???
                                    currentTokenType = Token.LITERAL_BACKQUOTE;
                                    break;
                                }
                            } // End of if (i<end-1 && array[i+1]=='{').

                            // If we reached the end of the variable, get out.
                            if (currentTokenType==Token.NULL || currentTokenType==Token.LITERAL_BACKQUOTE)
                                break;

                            // If we didn't find the '{' character, find the end of the variable...
                            // Increment first to skip the '$'.
                            while (++i<end) {
                                c = array[i];
                                if (!RSyntaxUtilities.isLetterOrDigit(c) && shellVariables.indexOf(c)==-1 && c!='_') {
                                    addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                                    if (c=='`') { // The only rub.
                                        addToken(text, i,i, Token.LITERAL_BACKQUOTE, newStartOffset+i);
                                        currentTokenType = Token.NULL;
                                        break;
                                    }
                                    else {
                                        currentTokenStart = i;
                                        currentTokenType = Token.LITERAL_BACKQUOTE;
                                        i--;
                                        break;
                                    }
                                }
                            }

                            // This only happens if we never found the end of the variable in the loop above.
                            // We "trick" this method so that the backquote string token is at the end.
                            if (i==end) {
                                addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.LITERAL_BACKQUOTE;
                            }

                            break;

                        // Otherwise, we're still in an unclosed string...
                        default:
                            backslash = false; // Need to set backslash to false here as a character was typed.

                    } // End of switch (c).

                    break;

                case Token.LITERAL_STRING_DOUBLE_QUOTE:

                    switch (c) {

                        case '\\':
                            backslash = !backslash;
                            break;

                        case '"':
                            if (!backslash) {
                                addToken(text, currentTokenStart,i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+currentTokenStart);
                                currentTokenType = Token.NULL;
                                // backslash is definitely false when we leave.
                                break;
                            }
                            backslash = false;
                            break;

                        // Variable in the double-quoted string...
                        case '$':

                            if (backslash==true) {
                                backslash = false;
                                break;
                            }

                            // Add the string up-to the variable.
                            addToken(text, currentTokenStart,i-1, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+currentTokenStart);
                            currentTokenType = Token.VARIABLE;
                            currentTokenStart = i;

                            // First check if the variable name is enclosed in '{' and '}' characters.
                            if (i<end-1 && array[i+1]=='{') {
                                i++; // Now we're on the '{' char.
                                while (++i<end) {
                                    if (array[i]=='}') {
                                        addToken(text, currentTokenStart,i, Token.VARIABLE, newStartOffset+currentTokenStart);
                                        i++;
                                        if (i<end) {
                                            c = array[i];
                                            if (c=='"') { // The only rub - double-quote right after variable.
                                                addToken(text, i,i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+i);
                                                currentTokenType = Token.NULL;
                                                break;
                                            }
                                            else { // Continue on with the string.
                                                currentTokenStart = i;
                                                currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                                                i--;
                                                break;
                                            }
                                        }
                                        else { // i==end = "trick" this method so that the string is continued to the next line.
                                            currentTokenStart = i;
                                            currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                                            break; // So we don't hit the condition below.
                                        }
                                    } // End of if (array[i]=='}').
                                } // End of while (++i<end).
                                if (i==end) { // Happens when '}' wasn't found...
                                    addToken(text, currentTokenStart,end-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                                    currentTokenStart = end; // ???
                                    currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                                    break;
                                }
                            } // End of if (i<end-1 && array[i+1]=='{').

                            // If we reached the end of the variable, get out.
                            if (currentTokenType==Token.NULL || currentTokenType==Token.LITERAL_STRING_DOUBLE_QUOTE)
                                break;

                            // If we didn't find the '{' character, find the end of the variable...
                            // Increment first to skip the '$'.
                            while (++i<end) {
                                c = array[i];
                                if (!RSyntaxUtilities.isLetterOrDigit(c) && shellVariables.indexOf(c)==-1 && c!='_') {
                                    addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                                    if (c=='"') { // The only rub.
                                        addToken(text, i,i, Token.LITERAL_STRING_DOUBLE_QUOTE, newStartOffset+i);
                                        currentTokenType = Token.NULL;
                                        break;
                                    }
                                    else {
                                        currentTokenStart = i;
                                        currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                                        i--;
                                        break;
                                    }
                                }
                            }

                            // This only happens if we never found the end of the variable in the loop above.
                            // We "trick" this method so that the double-quote string token is at the end.
                            if (i==end) {
                                addToken(text, currentTokenStart,i-1, Token.VARIABLE, newStartOffset+currentTokenStart);
                                currentTokenStart = i;
                                currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                            }

                            break;

                        // Otherwise, we're still in an unclosed string...
                        default:
                            backslash = false; // Need to set backslash to false here as a character was typed.

                    } // End of switch (c).

                    break;

            } // End of switch (currentTokenType).

        } // End of for (int i=offset; i<end; i++).

        switch (currentTokenType) {

            // Remember what token type to begin the next line with.
            case Token.LITERAL_BACKQUOTE:
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
            case Token.LITERAL_CHAR:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                break;

            // Do nothing if everything was okay.
            case Token.NULL:
                addNullToken();
                break;

            // All other token types don't continue to the next line...
            default:
                addToken(text, currentTokenStart,end-1, currentTokenType, newStartOffset+currentTokenStart);
                addNullToken();

        }

        // Return the first token in our linked list.
        return firstToken;

    }


}
