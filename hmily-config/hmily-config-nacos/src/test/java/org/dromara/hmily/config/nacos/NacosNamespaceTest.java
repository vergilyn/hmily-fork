package org.dromara.hmily.config.nacos;

import java.util.function.Supplier;

import org.dromara.hmily.common.utils.StringUtils;
import org.dromara.hmily.config.api.ConfigEnv;
import org.dromara.hmily.config.api.ConfigScan;
import org.dromara.hmily.config.api.entity.HmilyConfig;
import org.dromara.hmily.config.loader.ConfigLoader;
import org.dromara.hmily.config.loader.ServerConfigLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NacosClient.class)
public class NacosNamespaceTest {

	private static final String APP_NAME = "hmily-nacos-namespace";
	private NacosClient spyNacosClient = PowerMockito.spy(new NacosClient());

	/**
	 * Test nacos load.
	 */
	@Test
	public void testNacosLoadByNamespace() {
		ConfigScan.scan();
		ServerConfigLoader loader = new ServerConfigLoader();
		NacosConfigLoader nacosConfigLoader = new NacosConfigLoader(spyNacosClient);
		loader.load(ConfigLoader.Context::new, (context, config) -> {
			if (config != null) {
				if (StringUtils.isNoneBlank(config.getConfigMode())) {
					String configMode = config.getConfigMode();
					if (configMode.equals("nacos")) {
						nacosConfigLoader.load(context, this::assertTest);
					}
				}
			}
		});
	}

	private void assertTest(final Supplier<ConfigLoader.Context> supplier, final NacosConfig nacosConfig) {
		assertNotNull(nacosConfig);

		ConfigEnv env = ConfigEnv.getInstance();

		HmilyConfig hmilyConfig = env.getConfig(HmilyConfig.class);
		assertEquals(APP_NAME, hmilyConfig.getAppName());
	}

}
