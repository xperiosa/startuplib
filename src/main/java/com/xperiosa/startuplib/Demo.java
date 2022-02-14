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
package com.xperiosa.startuplib;

import com.xperiosa.startuplib.utils.FileUtils;
import com.xperiosa.startuplib.utils.OSUtils;
import java.io.File;

/**
 *
 * @author Xperiosa <https://github.com/xperiosa>
 */
public class Demo
{
	/**
	 * Main Demo
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		createStartup();
	}

	/**
	 * Create startup
	 *
	 * Creates startup entry for Windows, Mac & Others(Unix)
	 *
	 * @throws java.lang.Exception
	 */
	public static void createStartup() throws Exception
	{
		File jarFile = FileUtils.getJarFile();

		String fileName = jarFile.getName();

		if (!fileName.endsWith(".jar"))
		{
			System.out.println("Not running as jar..");
			return;
		}

		final StartupManager startup = new StartupManager(fileName, jarFile.toPath());

		if (OSUtils.isWindows)
		{
			startup.createWindowsRegistryStartup();
			startup.createWindowsShortcutStartup();
		}
		else if (OSUtils.isMac)
		{
			startup.createMacStartup();
		}
		else
		{
			startup.createUnixStartup();
		}
	}
}