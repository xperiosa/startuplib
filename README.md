# Startup Lib
Startup library for windows, mac & unix

### Example
```
		File jarFile = FileUtils.getJarFile();

		String fileName = jarFile.getName();

		if (!fileName.endsWith(".jar"))
		{
			System.out.println("Not running as jar..");
			return;
		}

		final StartupManager startup = new StartupManager(fileName, jarFile.toPath());

		if (startup.exists())
		{
			System.out.println("Existing startup found!");
			return;
		}

		if (OSUtils.isWindows)
		{
		    	//startup.createWindowsRegistryStartup();
			startup.createWindowsStartup();
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
```



