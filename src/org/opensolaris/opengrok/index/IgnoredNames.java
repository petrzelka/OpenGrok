/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 */
package org.opensolaris.opengrok.index;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * wrapper class for IgnoredFiles and IgnoredDirs
 *
 * @author Vladimir Kotal
 */
public class IgnoredNames {

    private final IgnoredFiles ignoredFiles;
    private final IgnoredDirs ignoredDirs;

    public IgnoredNames() {
        ignoredFiles = new IgnoredFiles();
        ignoredDirs = new IgnoredDirs();
    }

    public List<String> getItems() {
        List<String> twoLists = new ArrayList<>();
        twoLists.addAll(ignoredFiles.getItems());
        twoLists.addAll(ignoredDirs.getItems());
        return twoLists;
    }

    public void add(String pattern) {
        if (pattern.startsWith("f:")) {
            ignoredFiles.add(pattern.substring(2));
        } else if (pattern.startsWith("d:")) {
            ignoredDirs.add(pattern.substring(2));
        } else {
            // backward compatibility
            ignoredFiles.add(pattern);
        }
    }

    public void setItems(List<String> item) {
        clear();
        for (String s : item) {
            add(s);
        }
    }

    /**
     * Should the file be ignored or not?
     *
     * @param file the file to check
     * @return true if this file should be ignored, false otherwise
     */
    public boolean ignore(File file) {
        if (file.isFile()) {
            return ignoredFiles.match(file);
        } else {
            return ignoredDirs.match(file);
        }
    }

    /**
     * Should the file be ignored or not?
     *
     * @param name the name of the file to check
     * @return true if this pathname should be ignored, false otherwise
     */
    public boolean ignore(String name) {
        return ignoredFiles.match(name) || ignoredDirs.match(name);
    }

    public void clear() {
        ignoredFiles.clear();
        ignoredDirs.clear();
    }
}
