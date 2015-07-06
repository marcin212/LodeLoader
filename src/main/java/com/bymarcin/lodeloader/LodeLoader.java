package com.bymarcin.lodeloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;


@Mod(modid = LodeLoader.MODID, version = LodeLoader.VERSION, dependencies="required-after: ImmersiveEngineering")
public class LodeLoader
{
	public static final String MODID = "lodeloader";
	public static final String VERSION = "1.1";
	public static Logger logger = LogManager.getLogger(LodeLoader.MODID);

	@Instance(value = LodeLoader.MODID)
	public static LodeLoader instance;

	static {
		TypeToken<List<Lode>> typeToken = new TypeToken<List<Lode>>() {
		};
		collectionType = typeToken.getType();
	}

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static Type collectionType;
	private List<Lode> lodes;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		File conf = event.getSuggestedConfigurationFile();
		try {
			if (!conf.exists()) {
				FileUtils.touch(conf);
				lodes = new ArrayList<Lode>();
				String str = gson.toJson(lodes);
				FileUtils.writeStringToFile(conf, str);
			}
			String cfg = FileUtils.readFileToString(conf);
			lodes = gson.fromJson(cfg, collectionType);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JsonParseException ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		if (lodes != null) {
			for (Lode l : lodes) {
				if (l.isValid()) {
					ExcavatorHandler.addMineral(l.unlocalized_name, l.mineralChance, l.failChance, l.ores, l.chances);
					for (Entry<String, String> lang : l.localized_names.entrySet()) {
						HashMap<String, String> langList = new HashMap<String, String>();
						langList.put("desc.ImmersiveEngineering.info.mineral." + l.unlocalized_name, lang.getValue());
						LanguageRegistry.instance().injectLanguage(lang.getKey(), langList);
					}
				} else {
					logger.error("Wrong Lode Entry![" + l.unlocalized_name + "]");
				}
			}
		}
		ExcavatorHandler.recalculateChances();
	}

}
