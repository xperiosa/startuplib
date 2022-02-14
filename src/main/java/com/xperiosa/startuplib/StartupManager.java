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

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import mslinks.ShellLink;

/**
 *
 * @author Xperiosa <https://github.com/xperiosa>
 * 
 * @author Wille <https://github.com/wille/startuplib>
 */
public class StartupManager
{
	private final String name;
	private final Path filePath;

	/**
	 * Constructor
	 *
	 * @param name, name of startup entry
	 * @param filePath, File to auto-start
	 */
	public StartupManager(String name, Path filePath)
	{
		this.name = name;
		this.filePath = filePath;
	}

	/**
	 * Windows registry startup
	 *
	 * @throws Exception
	 */
	public void createWindowsRegistryStartup() throws Exception
	{
		final String command;

		if (filePath.toFile().getName().endsWith(".jar"))
		{
			String javaHome = System.getProperty("java.home") + "\\bin\\javaw.exe";
			command = "\"" + javaHome + "\"" + " -jar " + "\"" + filePath + "\"";
		}
		else
		{
			command = "\"" + this.filePath.toString() + "\"";
		}
		
		// Write to registry
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", name, command);
	}

	/**
	 * Windows startup using shortcut
	 *
	 * @throws Exception
	 */
	public void createWindowsShortcutStartup() throws Exception
	{
		final File file = filePath.toFile();
		final String shortcut = String.format("C:\\Users\\" + System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\" + name + ".lnk");

		// Create shortcut
		ShellLink.createLink(file.getAbsolutePath(), shortcut);
	}

	/**
	 * Mac startup
	 *
	 * @throws Exception
	 */
	public void createMacStartup() throws Exception
	{
		final File startupDir = new File(System.getProperty("user.home") + "/Library/LaunchAgents/");

		if (!startupDir.exists())
		{
			startupDir.mkdirs();
		}

		final File startupEntryFile = new File(startupDir, this.name + ".plist");

		try (PrintWriter out = new PrintWriter(new FileWriter(startupEntryFile)))
		{
			out.println("<plist version=\"1.0\">");
			out.println("<dict>");
			out.println("\t<key>Label</key>");
			out.println("\t<string>" + this.name + "</string>");
			out.println("\t<key>ProgramArguments</key>");
			out.println("\t<array>");
			if (this.filePath.toFile().getName().endsWith(".jar"))
			{
				out.println("\t\t<string>java</string>");
				out.println("\t\t<string>-jar</string>");
			}
			out.println("\t\t<string>" + this.filePath + "</string>");
			out.println("\t</array>");
			out.println("\t<key>RunAtLoad</key>");
			out.println("\t<true/>");
			out.println("</dict>");
			out.println("</plist>");
		}
	}

	/**
	 * Unix startup
	 *
	 * @throws Exception
	 */
	public void createUnixStartup() throws Exception
	{
		final File startupDir = new File(System.getProperty("user.home") + "/.config/autostart/");

		if (!startupDir.exists())
		{
			startupDir.mkdirs();
		}

		final File startupEntryFile = new File(startupDir, this.name + ".desktop");

		try (PrintWriter out = new PrintWriter(new FileWriter(startupEntryFile)))
		{
			out.println("[Desktop Entry]");
			out.println("Type=Application");
			out.println("Name=" + this.name);
			if (this.filePath.toFile().getName().endsWith(".jar"))
			{
				out.println("Exec=java -jar '" + filePath + "'");
			}
			else
			{
				out.println("Exec='" + this.filePath + "'");
			}
			out.println("Terminal=false");
			out.println("NoDisplay=true");
		}

		String[] cmd = new String[]
		{
			"chmod", "+x", this.filePath.toString()
		};
		Runtime.getRuntime().exec(cmd);
	}

	/**
	 * Delete windows startup entry from registry
	 */
	public void deleteWindowsRegistryStartup()
	{
		if (this.windowsRegistryStartupExists())
		{
			Advapi32Util.registryDeleteKey(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", name);
		}
	}

	/**
	 * Delete windows shortcut startup entry from startup folder
	 */
	public void deleteWindowsShortcutStartup()
	{
		if (this.windowsShortcutStartupExists())
		{
			Paths.get(String.format("C:\\Users\\" + System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\" + name + ".lnk")).toFile().delete();
		}
	}

	/**
	 * Delete mac startup entry
	 */
	public void deleteMacStartup()
	{
		if (this.macStartupExists())
		{
			final File startupDir = new File(System.getProperty("user.home") + "/Library/LaunchAgents/");
			final File startupEntryFile = new File(startupDir, this.name + ".plist");
			startupEntryFile.delete();
		}
	}

	/**
	 * Delete unix startup entry
	 */
	public void deleteUnixStartup()
	{
		if (this.unixStartupExists())
		{
			final File startupDir = new File(System.getProperty("user.home") + "/.config/autostart/");
			final File startupEntryFile = new File(startupDir, this.name + ".desktop");
			startupEntryFile.delete();
		}
	}

	public boolean windowsRegistryStartupExists()
	{
		return Advapi32Util.registryGetValues(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run").containsKey(name);

	}

	public boolean windowsShortcutStartupExists()
	{
		return Paths.get(String.format("C:\\Users\\" + System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\" + name + ".lnk")).toFile().exists();

	}

	public boolean macStartupExists()
	{
		final File startupDir = new File(System.getProperty("user.home") + "/Library/LaunchAgents/");
		return new File(startupDir, this.name + ".plist").exists();
	}

	public boolean unixStartupExists()
	{
		final File startupDir = new File(System.getProperty("user.home") + "/.config/autostart/");
		return new File(startupDir, this.name + ".desktop").exists();
	}
}
