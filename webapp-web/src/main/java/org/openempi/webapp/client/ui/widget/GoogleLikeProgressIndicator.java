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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A {@link ProgressIndicator} implementation which mimic the progress indicator used by Google in their product. This
 * indicator basically shows a label on to top-center of the screen.
 *
 * Borrowed from Beginning Google Web Toolkit Book
 */
public class GoogleLikeProgressIndicator extends PopupPanel implements ProgressIndicator {

    private final Label messageLabel;

    /**
     * Constructs a new GoogleLikeProgressIndicator with "Loading" as a default initial message.
     */
    public GoogleLikeProgressIndicator() {
        this(DEFAULT_MESSAGE);
    }

    /**
     * Constructs a new GoogleLikeProgressIndicator with a given initial message.
     *
     * @param message The message the indicator should initialy show.
     */
    public GoogleLikeProgressIndicator(String message) {
        super(false, true);
		messageLabel = new Label(message);
		messageLabel.setStyleName("Message");
		setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                int x = Window.getClientWidth()/2 - offsetWidth/2;
                setPopupPosition(x, 0);
            }
        });
        setWidget(messageLabel);
        setStyleName("GoogleLikeProgressIndicator");
    }

    /**
     * {@inheritDoc}
     */
    public void setMessage(String message) {
        messageLabel.setText(message);
    }

}
