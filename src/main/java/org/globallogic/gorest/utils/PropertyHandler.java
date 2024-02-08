package org.globallogic.gorest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyHandler {

    private final Properties properties = new Properties();

        private Properties readFile (String file){

            // Load the properties file
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(file)) {
                if (input == null) {
                    throw new IOException("Unable to find " + file);
                }
                properties.load(input);
            } catch (IOException exception) {
                Logger.getLogger(exception.getMessage());
            }
            return properties;
        }

        public String get(String file, String key) {
            return readFile(file).getProperty(key);
        }

        public String getConfig(String key) {
            return readFile("config.properties").getProperty(key);
        }

        public String getTestData(String key) {
            return readFile("test-data.properties").getProperty(key);
        }



}
