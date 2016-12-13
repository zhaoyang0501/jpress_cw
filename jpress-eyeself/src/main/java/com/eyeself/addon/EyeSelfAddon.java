package com.eyeself.addon;

import com.jfinal.core.Controller;

import io.jpress.core.addon.Addon;
import io.jpress.core.addon.Hook;
import io.jpress.core.addon.Hooks;

public class EyeSelfAddon extends Addon {

	@Override
	public boolean onStart() {
		return true;
	}

	@Override
	public boolean onStop() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Hook(Hooks.INDEX_RENDER_BEFORE)
	public void indexRenderBefore(Controller controller) {
		
	}
	

}
