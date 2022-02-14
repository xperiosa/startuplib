/*
 * Copyright (c) 2022, Xperiosa <https://github.com/xperiosa>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.xperiosa.startuplib.utils;

import java.util.Locale;

/**
 *
 * @author Xperiosa <https://github.com/xperiosa>
 */
public class OSUtils
{
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

	public static final boolean isWindows = OS_NAME.contains("win");
	public static final boolean isMac = OS_NAME.contains("mac") || OS_NAME.contains("darwin");
	public static final boolean isLinux = OS_NAME.contains("linux");

	public static final boolean isUnix = OS_NAME.contains("nix")
			|| OS_NAME.contains("nux")
			|| OS_NAME.contains("aix")
			|| OS_NAME.contains("ix")
			|| OS_NAME.contains("bsd")
			|| OS_NAME.contains("hp-ux");

	public static final boolean isSolaris = OS_NAME.contains("sunos") || OS_NAME.contains("solaris");
}
