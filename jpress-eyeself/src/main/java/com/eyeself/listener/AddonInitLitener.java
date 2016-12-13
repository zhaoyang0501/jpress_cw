package com.eyeself.listener;

import com.eyeself.addon.EyeSelfAddon;

import io.jpress.core.addon.AddonInfo;
import io.jpress.core.addon.AddonManager;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;

@Listener(action = AddonManager.MESSAGE_ON_ADDON_LOAD_FINISHED)
public class AddonInitLitener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		AddonManager manager = message.getData();

		AddonInfo addon = new AddonInfo();
		addon.setId("eyeself");
		addon.setAddon(new EyeSelfAddon());

		manager.registerAddon(addon);

	}

}
