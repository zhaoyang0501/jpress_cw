package com.eyeself.listener;

import com.eyeself.tag.SliderTag;

import io.jpress.core.Jpress;
import io.jpress.message.Actions;
import io.jpress.message.Message;
import io.jpress.message.MessageListener;
import io.jpress.message.annotation.Listener;

@Listener(action = Actions.JPRESS_STARTED)
public class StartedListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		System.out.println(">>>>>>>>>>>>" + message.getAction());
		
		Jpress.addTag("slider", new SliderTag());
		

	}


}
