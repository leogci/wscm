package com.gci.siarp.webapp.ui;

import com.gci.siarp.api.annotation.SiarpUIComponent;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SiarpUIComponent
@PreserveOnRefresh
@Theme("siarp")
@SuppressWarnings("serial")
public class SiarpUI extends UI {



	

	@Override
	protected void init(VaadinRequest request) {
	
		getPage().setTitle("SIARP");
	}


}
