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
public class StartupManagerLite
{
	private final String name;
	private final Path filePath;

	/**
	 * Constructor
	 *
	 * @param name, name of startup entry
	 * @param filePath, File to auto-start
	 */
	public StartupManagerLite(String name, Path filePath)
	{
		this.name = name;
		this.filePath = filePath;
	}

	/**
	 * Windows startup, Create shortcut file in startup folder
	 *
	 * @throws Exception
	 */
	public void createWindowsStartup() throws Exception
	{
		// Create shortcut
		ShellLink.createLink(filePath.toFile().getAbsolutePath(), this.getWindowsStartupFile().getAbsolutePath());
	}

	/**
	 * Mac startup, Create file in startup folder
	 *
	 * @throws Exception
	 */
	public void createMacStartup() throws Exception
	{
		try (PrintWriter out = new PrintWriter(new FileWriter(getMacStartupFile())))
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
	 * Unix startup, Create file in startup folder
	 *
	 * @throws Exception
	 */
	public void createUnixStartup() throws Exception
	{
		try (PrintWriter out = new PrintWriter(new FileWriter(this.getUnixStartupFile())))
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
	 * Delete windows shortcut file from startup folder
	 */
	public void deleteWindowsStartup()
	{
		if (getWindowsStartupFile().exists())
		{
			getWindowsStartupFile().delete();
		}
	}

	/**
	 * Delete mac startup file from startup folder
	 */
	public void deleteMacStartup()
	{
		if (getMacStartupFile().exists())
		{
			getMacStartupFile().delete();
		}
	}

	/**
	 * Delete unix startup file from startup folder
	 */
	public void deleteUnixStartup()
	{
		if (getUnixStartupFile().exists())
		{
			getUnixStartupFile().delete();
		}
	}

	/**
	 * Get windows startup shortcut file
	 * 
	 * @return shortcut file from startup folder
	 */
	public File getWindowsStartupFile()
	{
		return Paths.get(String.format(System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\" + name + ".lnk")).toFile();
	}

	/**
	 * Get mac startup file
	 * 
	 * @return mac .plist file from LaunchAgents folder
	 */
	public File getMacStartupFile()
	{
		final File startupDir = new File(System.getProperty("user.home") + "/Library/LaunchAgents/");
		if (!startupDir.exists())
		{
			startupDir.mkdirs();
		}
		return new File(startupDir, this.name + ".plist");
	}

	/**
	 * Get unix startup file
	 * 
	 * @return unix startup file from autostart folder
	 */
	public File getUnixStartupFile()
	{
		final File startupDir = new File(System.getProperty("user.home") + "/.config/autostart/");
		if (!startupDir.exists())
		{
			startupDir.mkdirs();
		}
		return new File(startupDir, this.name + ".desktop");
	}
}
