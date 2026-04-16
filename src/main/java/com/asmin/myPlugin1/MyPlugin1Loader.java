package com.asmin.myPlugin1;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;

class MyPlugin1Loader implements PluginLoader {

    @Override
    public void classloader(final PluginClasspathBuilder builder) {
        // Add dynamically loaded libraries here
    }
}
