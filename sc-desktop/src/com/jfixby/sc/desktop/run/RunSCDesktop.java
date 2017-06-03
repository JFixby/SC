
package com.jfixby.sc.desktop.run;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.FokkerLwjglApplication;
import com.badlogic.gdx.backends.lwjgl.FokkerLwjglApplicationConfiguration;
import com.jfixby.r3.api.EngineParams;
import com.jfixby.r3.api.EngineParams.Assets;
import com.jfixby.r3.api.EngineParams.Settings;
import com.jfixby.r3.api.RedTriplane;
import com.jfixby.r3.api.shader.R3Shader;
import com.jfixby.r3.api.ui.UnitsSpawner;
import com.jfixby.r3.api.ui.unit.layer.LayerUtils;
import com.jfixby.r3.ext.api.patch18.P18;
import com.jfixby.r3.ext.p18t.red.RedP18Terrain;
import com.jfixby.r3.ext.red.terrain.RedTerrain;
import com.jfixby.r3.ext.scene2d.api.Scene2DSpawner;
import com.jfixby.r3.ext.scene2d.red.RedScene2DSpawner;
import com.jfixby.r3.ext.scene2d.red.Scene2DPackageLoader;
import com.jfixby.r3.fokker.Fokker;
import com.jfixby.r3.fokker.FokkerStarter;
import com.jfixby.r3.fokker.RedUnitSpawner;
import com.jfixby.r3.fokker.adaptor.GdxAdaptor;
import com.jfixby.r3.fokker.api.FokkerEngineParams;
import com.jfixby.r3.fokker.api.UnitsMachineExecutor;
import com.jfixby.r3.fokker.api.starter.FokkerStarterConfig;
import com.jfixby.r3.fokker.core.shader.FokkerShaderPackageReader;
import com.jfixby.r3.fokker.core.starter.FokkerStarterConfigAsset;
import com.jfixby.r3.fokker.core.starter.FokkerStarterConfigReader;
import com.jfixby.r3.fokker.render.raster.FokkerTextureLoader;
import com.jfixby.r3.fokker.unit.layers.RedLayerUtils;
import com.jfixby.r3.fokker.unit.shader.R3FokkerShader;
import com.jfixby.r3.ui.RedUIManager;
import com.jfixby.r3.uiact.UIEventsManager;
import com.jfixby.rana.api.asset.AssetHandler;
import com.jfixby.rana.api.asset.AssetsConsumer;
import com.jfixby.rana.api.asset.AssetsManager;
import com.jfixby.rana.api.asset.AssetsPurgeResult;
import com.jfixby.rana.api.asset.LoadedAssets;
import com.jfixby.rana.api.loader.PackagesLoader;
import com.jfixby.rana.api.pkg.PackagesBank;
import com.jfixby.rana.api.pkg.PackagesManager;
import com.jfixby.rana.api.pkg.PackagesTank;
import com.jfixby.rana.red.loader.RedPackagesLoader;
import com.jfixby.red.engine.core.resources.RedAssetsManager;
import com.jfixby.red.engine.core.resources.RedLoadedAssets;
import com.jfixby.red.triplane.resources.fsbased.RedPackageManager;
import com.jfixby.red.triplane.resources.fsbased.RedResourcesManagerSpecs;
import com.jfixby.sc.game.SCTheGame;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;
import com.jfixby.scarabei.api.collisions.Collisions;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileSystemSandBox;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.input.UserInput;
import com.jfixby.scarabei.api.java.gc.GCFisher;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.sys.SystemInfo;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.api.ver.Version;
import com.jfixby.scarabei.gson.GoogleGson;
import com.jfixby.scarabei.red.collisions.RedCollisionsAlgebra;
import com.jfixby.scarabei.red.filesystem.sandbox.RedFileSystemSandBox;
import com.jfixby.scarabei.red.input.RedInput;
import com.jfixby.util.iso.api.Isometry;
import com.jfixby.util.iso.red.RedIsometry;
import com.jfixby.util.p18t.api.P18Terrain;
import com.jfixby.util.patch18.red.RedP18;
import com.jfixby.util.terain.test.api.palette.Terrain;
import com.jfixby.utl.pizza.api.Pizza;
import com.jfixby.utl.pizza.red.RedPizza;

public class RunSCDesktop {

	private static final String INSTALLATION_ID_FILE_NAME = "com.jfixby.sc.iid";

	public static void main (final String[] arg) {

		ScarabeiDesktop.deploy();

		Json.installComponent(new GoogleGson());
		// bankFolder.listAllChildren().print("files");

		final File assets_cache_folder = LocalFileSystem.ApplicationHome().child("assets-cache");
		final File assets_folder = LocalFileSystem.ApplicationHome().child("assets");

		final RedResourcesManagerSpecs resman_spec = new RedResourcesManagerSpecs();
		resman_spec.assets_cache_folder = assets_cache_folder;
		resman_spec.assets_folder = assets_folder;
		resman_spec.readResourcesConfigFile = true;
		final RedPackageManager resman = new RedPackageManager(resman_spec);
		PackagesManager.installComponent(resman);
		LoadedAssets.installComponent(new RedLoadedAssets());
		AssetsManager.installComponent(new RedAssetsManager());
		PackagesLoader.installComponent(new RedPackagesLoader());
		FileSystemSandBox.installComponent(new RedFileSystemSandBox());

		final PackagesBank resources = PackagesManager.getBank(Names.newID("com.jfixby.sc.assets.local"));
// resources.printAllIndexes();
		final PackagesTank tank = resources.getTank("tank-0");

// PackagesManager.getResourcesGroup(name)

// PackagesManager.printAllIndexes();

		PackagesLoader.registerPackageReader(new FokkerStarterConfigReader());
		final ID starterID = Names.newID("com.jfixby.sc.fokker.starter.cfg");
		try {
			AssetsManager.autoResolveAsset(starterID);
		} catch (final IOException e) {
			Err.reportError(e);
			Sys.exit();
		}

		final AssetsConsumer consumer = new AssetsConsumer() {};
// cfg.title = "Tinto the Fox";
// cfg.useGL30 = false;
// cfg.width = 1024 + 60 * 0;
// cfg.height = 768 + 60 * 0;
		final AssetHandler starterAsset = LoadedAssets.obtainAsset(starterID, consumer);
		final FokkerStarterConfigAsset starterConfig = starterAsset.asset();

		starterConfig.print();

		final String title = starterConfig.getValue(FokkerStarterConfig.TITLE);
		final String width = starterConfig.getValue(FokkerStarterConfig.width);
		final String height = starterConfig.getValue(FokkerStarterConfig.height);
		final String useGL30 = starterConfig.getValue(FokkerStarterConfig.useGL30);

		LoadedAssets.releaseAsset(starterAsset, consumer);

		final AssetsPurgeResult purgeResult = AssetsManager.purge();

		purgeResult.print();

// Sys.exit();
// tintoLoaderUnitID = Names.newID("");
// final UnitHandler unitHandler = UnitOperator.deployUnit(tintoLoaderUnitID);
// final UnitHandler oldUnit = UnitOperator.push(unitHandler);
// oldUnit.dispose();
		SystemSettings.setExecutionMode(ExecutionMode.EARLY_DEVELOPMENT);
		SystemSettings.setFlag(Settings.PrintLogMessageOnMissingSprite, true);
		SystemSettings.setFlag(Settings.PrintLogMessageOnDuplicateAssetRequest, false);
		SystemSettings.setFlag(Settings.ExitOnMissingSprite, false);
		SystemSettings.setFlag(Settings.AllowMissingRaster, true);
// SystemSettings.setFlag(AssetsManager.UseAssetSandBox, false);
// SystemSettings.setFlag(AssetsManager.ReportUnusedAssets, false);
// SystemSettings.setFlag(AssetsManagerFlags.AutoresolveDependencies, true);
// SystemSettings.setFlag(Settings.DisableLogo, true);

		SystemSettings.setStringParameter(FokkerEngineParams.TextureFilter.Mag,
			com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest + "");
		SystemSettings.setStringParameter(FokkerEngineParams.TextureFilter.Min,
			com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest + "");
		SystemSettings.setStringParameter(Assets.DefaultFont, "Arial");
		SystemSettings.setLongParameter(Assets.DEFAULT_LOGO_FADE_TIME, 2000L);
		SystemSettings.setStringParameter(Assets.CLEAR_SCREEN_COLOR_ARGB, "#FF070e0c");
// SystemSettings.setStringParameter(Assets.CLEAR_SCREEN_COLOR_ARGB, "#FFFF0eFc");
		SystemSettings.setLongParameter(GCFisher.DefaultBaitSize, 1 * 1024 * 1024);

		SystemSettings.setStringParameter(Version.Tags.PackageName, SCVersion.packageName);
		SystemSettings.setStringParameter(Version.Tags.VersionCode, SCVersion.versionCode + "");
		SystemSettings.setStringParameter(Version.Tags.VersionName, SCVersion.versionName);
// installResources();
// deployAnalytics();
		UserInput.installComponent(new RedInput());
		Scene2DSpawner.installComponent(new RedScene2DSpawner());
		R3Shader.installComponent(new R3FokkerShader());

		LayerUtils.installComponent(new RedLayerUtils());

		PackagesLoader.registerPackageReader(new Scene2DPackageLoader());
		PackagesLoader.registerPackageReader(new FokkerShaderPackageReader());
		PackagesLoader.registerPackageReader(new FokkerTextureLoader());

		Collisions.installComponent(new RedCollisionsAlgebra());
		RedTriplane.installComponent(new Fokker());
		RedTriplane.setGameStarter(new SCTheGame());
		UnitsSpawner.installComponent(new RedUnitSpawner());

		{
			final SystemInfo deviceinfo = Sys.getSystemInfo();
			deviceinfo.putValue(EngineParams.Version.Name, RedTriplane.VERSION().getName());
			deviceinfo.putValue(EngineParams.Version.BuildID, RedTriplane.VERSION().getBuildID());
			deviceinfo.putValue(EngineParams.Version.HomePage, RedTriplane.VERSION().getHomePage());

			deviceinfo.print();

			SystemSettings.printSystemParameters();

		}

		final RedUIManager tinto_ui_starter = new RedUIManager();
		tinto_ui_starter.startEventsMachine();
		UIEventsManager.installComponent(tinto_ui_starter);

		RedTriplane.setGameStarter(new SCTheGame());

		P18Terrain.installComponent(new RedP18Terrain());
		P18.installComponent(new RedP18());
		Terrain.installComponent(new RedTerrain());
		Pizza.installComponent(new RedPizza());
		Isometry.installComponent(new RedIsometry());
// final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
// new LwjglApplication(new EntryPoint(), config);

		final com.jfixby.r3.fokker.FokkerStarterSpecs config = FokkerStarter.newRedTriplaneConfig();
		config.setEngineAssembler(new SCDesktopAssembler());

		final FokkerStarter triplane_starter = FokkerStarter.newRedTriplane(config);
		final UnitsMachineExecutor machine = triplane_starter.getUnitsMachineExecutor();

		final GdxAdaptor adaptor = new GdxAdaptor(machine);

		final FokkerLwjglApplicationConfiguration cfg = new FokkerLwjglApplicationConfiguration();
		cfg.title = title;
		cfg.useGL30 = Boolean.parseBoolean(useGL30);
		cfg.width = Integer.parseInt(width);
		cfg.height = Integer.parseInt(height);
// cfg.vSyncEnabled = false;
// cfg.r = 1;
// cfg.g = 1;
// cfg.b = 1;
// cfg.a = 1;
// cfg.overrideDensity = 10;
// cfg.foregroundFPS = 60;

		final ApplicationListener gdx_listener = adaptor.getGDXApplicationListener();

		// gdx_listener = new HttpRequestTest();
		// GdxEntryPoint point = new GdxEntryPoint();
		// new LwjglApplication(point, cfg);
		new FokkerLwjglApplication(gdx_listener, cfg);
	}

}
