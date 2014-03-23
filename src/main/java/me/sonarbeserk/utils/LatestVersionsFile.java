package me.sonarbeserk.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.InputStream;

/***********************************************************************************************************************
 *
 * BeserkUtils - Premade classes for use in my bukkit plugins
 * ===========================================================================
 *
 * Copyright (C) 2014 by SonarBeserk
 * https://github.com/SonarBeserk/BeserkUtils
 *
 ***********************************************************************************************************************
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************************************************************/
public class LatestVersionsFile {

    private JavaPlugin plugin = null;

	private FileConfiguration versions = null;
	private File versionsFile = null;

	public LatestVersionsFile(JavaPlugin plugin) {
		
		this.plugin = plugin;
	}

    /**
     * Reloads the data file
     */
	public void reload() {
		
		InputStream defConfigStream = plugin.getResource("latest-versions.yml");
		
		if(defConfigStream != null) {
			
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			versions = defConfig;
		} else {
			
			plugin.getLogger().severe("Unable to load version data.");
			return;
		}
	}
	
	private FileConfiguration getFileConfiguration() {
		
		if(versions == null) {
			
			reload();
		}
		
		return versions;
	}

    /**
     * Saves the default version of the data file if it was not found
     */
	/*
    private void saveDefault() {
		
		if(versionsFile == null) {
			
			versionsFile = new File(plugin.getDataFolder() + "/" + "latest-versions.yml");
		}
		
		if(!dataFile.exists()) {
			
			plugin.saveResource("latest-versions.yml", false);
			return;
		}
	}
	*/

    /**
     * Saves the current state of the versions file, also destroys any comments in the file
     */

    /*
    public void save() {
		
		if((versions == null)||(versionsFile == null)) {return;}
		
		try {
			
			getFileConfiguration().save(versionsFile);
		} catch (IOException ex) {
			
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + versionsFile, ex);
		}
	}
	*/

    /**
     * Sets the value at the path specified
     * @param path the path to set at
     * @param value the value to set
     */
    /*
    public void set(String path, Object value) {

        versions.set(path, value);
    }
    */

    /**
     * Returns a version entry from the path specified
     * @param path the path to the entry
     * @return a version entry from the path specified
     */

    public Object get(String path) {

        return getFileConfiguration().get(path);
    }

    /**
     * Returns the configuration section at the path specified
     * @param path the path to the configuration section
     * @return the configuration section at the path specified
     */
    public ConfigurationSection getConfigurationSection(String path) {

        return getFileConfiguration().getConfigurationSection(path);
    }

    /**
     * Creates a configuration section with a map at the path specified
     * @param path the path to create the section at
     * @param map the value to set
     */
    /*
    public void createSection(String path, Map<?, ?> map) {

        getFileConfiguration().createSection(path, map);
    }
    */
}
