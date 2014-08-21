/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openempi.webapp.client.ui.widget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class TitledPanel extends Composite {

	private final static int TITLE_ROW = 0;
	private final static int CONTENT_ROW = 1;

    private final static String STYLE_NAME = "TitledPanel";
	private final static String TITLE_STYLE_NAME = "TitleText";
	private final static String CONTENT_STYLE_NAME = "Content";
	private final static String TOOLBAR_STYLE_NAME = "Toolbar";
	private final static String TOOL_BUTTON_STYLE_NAME = "ToolButton";
	
	private final Label titleLabel;
	private final Grid grid;
	private final DockPanel title;
	private final HorizontalPanel toolbar;

	public TitledPanel() {
		this("");
	}
	
	public TitledPanel(String titleText) {
		this(titleText, null);
	}

	public TitledPanel(String titleText, Widget content) {
		titleLabel = new Label(titleText);
        titleLabel.setStyleName(TITLE_STYLE_NAME);
		toolbar = new HorizontalPanel();
		toolbar.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		toolbar.setSpacing(0);
		toolbar.setBorderWidth(0);
		toolbar.setStyleName(TOOLBAR_STYLE_NAME);
		
		title = new DockPanel();
		title.setStyleName(TITLE_STYLE_NAME);
		title.add(titleLabel, DockPanel.CENTER);
		title.setCellVerticalAlignment(titleLabel, DockPanel.ALIGN_MIDDLE);
		title.setCellWidth(titleLabel, "100%");
		title.add(toolbar, DockPanel.EAST);
		title.setWidth("100%");
		
        grid = new Grid(2, 1);
        grid.setBorderWidth(0);
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.setWidget(TITLE_ROW, 0, title);
        grid.getCellFormatter().setWidth(TITLE_ROW, 0, "100%");
        if (content != null) {
        	grid.setWidget(CONTENT_ROW, 0, content);
        }
        grid.getCellFormatter().setWidth(CONTENT_ROW, 0, "100%");
        grid.getCellFormatter().setHeight(CONTENT_ROW, 0, "100%");
        grid.getCellFormatter().setStyleName(CONTENT_ROW, 0, CONTENT_STYLE_NAME);
        
        initWidget(grid);
        setStyleName(STYLE_NAME);
	}
	
	public void setTitleText(String text) {
		titleLabel.setText(text);
	}
	
	public void setContent(Widget content) {
		grid.setWidget(CONTENT_ROW, 0, content);
	}
	
	public void setContentVerticalAlignment(HasVerticalAlignment.VerticalAlignmentConstant alignment) {
		grid.getCellFormatter().setVerticalAlignment(CONTENT_ROW, 0, alignment);
	}
	
	public void setContentHorizontalAlignment(HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
		grid.getCellFormatter().setHorizontalAlignment(CONTENT_ROW, 0, alignment);
	}
	
	public PushButton addToolButton(String text, String title, ClickListener clickListener) {
		PushButton button = new PushButton(text, clickListener);
		if (title != null) {
			button.setTitle(title);
		}
		addToolButton(button);
		return button;
	}
	
	public PushButton addToolButton(Image image, ClickListener clickListener) {
		PushButton button = new PushButton(image, clickListener);
		addToolButton(button);
		return button;
	}
	
	public void addToolButton(PushButton button) {
		button.setStyleName(TOOL_BUTTON_STYLE_NAME);
		toolbar.add(button);
		toolbar.setCellVerticalAlignment(button, HorizontalPanel.ALIGN_MIDDLE);
	}

}

