package uk.ac.ncl.cemdit.controller;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A <em>prefix</em> based filename filter.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @version <tt>$Revision: 1958 $</tt>
 */
public class FilenamePrefixFilter extends FileFilter {

    String description;
    String prefix;

    public FilenamePrefixFilter(String description, String prefix) {
        this.description = description;
        this.prefix = prefix;
    }

    @Override
    public boolean accept(File f) {
        if (f.getName().startsWith(prefix) || f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

}