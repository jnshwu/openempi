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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that can hold only one widget in its center point.
 *
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class CenterPanel extends Composite {

    private DockPanel container;

    private Widget content;

    /**
     * Constructs a new CenterPanel with no content.
     */
    public CenterPanel() {
        this(null);
    }

    /**
     * Constructs a new CenterPanel with a given content.
     *
     * @param content The content of this panel.
     */
    public CenterPanel(Widget content) {
        container = new DockPanel();
        if (content != null) {
            setContent(content);
        }
        initWidget(container);
        setStyleName("CenterPanel");
    }

    /**
     * Sets the content of this panel. The content will be placed in the center of this panel.
     *
     * @param content The content of this panel.
     */
    public void setContent(Widget content) {
        if (this.content != null) {
            container.remove(this.content);
        }
        this.content = content;
        container.add(content, DockPanel.CENTER);
        container.setCellHorizontalAlignment(content, DockPanel.ALIGN_CENTER);
        container.setCellVerticalAlignment(content, DockPanel.ALIGN_MIDDLE);
    }

    /**
     * Returns the content of this panel.
     *
     * @return The content of this panel.
     */
    public Widget getContent() {
        return content;
    }

}
