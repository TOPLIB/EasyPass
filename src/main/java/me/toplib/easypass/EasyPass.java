package me.toplib.easypass;

import me.toplib.easypass.core.ConfigManager;
import me.toplib.easypass.core.EventBus;
import me.toplib.easypass.core.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * EasyPass - Modular Battle Pass Plugin
 * 
 * Architecture:
 * - Modular system: All features are independent modules
 * - Event-driven: Modules communicate via EventBus
 * - Service-oriented: Core services accessible via API
 * - Storage-agnostic: Multiple storage backends supported
 */
public final class EasyPass extends JavaPlugin {

    private static EasyPass instance;
    
    // Core components
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private EventBus eventBus;

    @Override
    public void onLoad() {
        instance = this;
        
        // Initialize core components
        this.eventBus = new EventBus();
        this.configManager = new ConfigManager(this);
        this.moduleManager = new ModuleManager(this);
        
        getLogger().info("EasyPass core loaded");
    }

    @Override
    public void onEnable() {
        // Load configuration
        saveDefaultConfig();
        
        // Register modules here
        // Example: moduleManager.registerModule(new BattlePassModule(this));
        // Example: moduleManager.registerModule(new QuestModule(this));
        // Example: moduleManager.registerModule(new RewardModule(this));
        
        // Load and enable all modules
        moduleManager.loadModules();
        moduleManager.enableModules();
        
        getLogger().info("EasyPass enabled with " + moduleManager.getModules().size() + " modules");
    }

    @Override
    public void onDisable() {
        // Disable all modules in reverse order
        if (moduleManager != null) {
            moduleManager.disableModules();
        }
        
        // Clear event bus
        if (eventBus != null) {
            eventBus.clearAll();
        }
        
        getLogger().info("EasyPass disabled");
    }

    public static EasyPass getInstance() {
        return instance;
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public EventBus getEventBus() {
        return eventBus;
    }
}
