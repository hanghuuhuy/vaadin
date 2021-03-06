/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.server.component.abstractcomponent;

import java.io.File;
import java.util.Locale;

import junit.framework.TestCase;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.declarative.DesignContext;

/**
 * Test case for writing the attributes of the AbstractComponent to design
 * 
 * @author Vaadin Ltd
 */
public class WriteDesignTest extends TestCase {

    private DesignContext ctx;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctx = new DesignContext();
    }

    public void testSynchronizeId() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setId("testId");
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("testId", design.attr("id"));
    }

    public void testSynchronizePrimaryStyleName() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setPrimaryStyleName("test-style");
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("test-style", design.attr("primary-style-name"));
    }

    public void testSynchronizeCaption() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setCaption("test-caption");
        component.writeDesign(design, ctx);
        // We only changed the caption, which is not
        // an attribute.
        assertEquals(0, design.attributes().size());
        assertEquals("test-caption", design.html());
    }

    public void testSynchronizeLocale() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setLocale(new Locale("fi", "FI"));
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("fi_FI", design.attr("locale"));
    }

    public void testSynchronizeExternalIcon() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component
                .setIcon(new ExternalResource("http://example.com/example.gif"));
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("http://example.com/example.gif", design.attr("icon"));
    }

    public void testSynchronizeThemeIcon() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setIcon(new ThemeResource("example.gif"));
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("theme://example.gif", design.attr("icon"));
    }

    public void testSynchronizeFileResource() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setIcon(new FileResource(new File("img/example.gif")));
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("img/example.gif", design.attr("icon"));
    }

    public void testSynchronizeImmediate() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        // no immediate attribute should be written before setting immediate to
        // some value
        component.writeDesign(design, ctx);
        assertFalse(design.hasAttr("immediate"));
        component.setImmediate(true);
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("true", design.attr("immediate"));
    }

    public void testSynchronizeImmediateByDefault() {
        Element design = createDesign();
        TabSheet byDefaultImmediate = new TabSheet();
        // no immediate attribute should be written before setting immediate to
        // false
        byDefaultImmediate.writeDesign(design, ctx);
        assertFalse(design.hasAttr("immediate"));
        byDefaultImmediate.setImmediate(false);
        byDefaultImmediate.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("false", design.attr("immediate"));
    }

    public void testSynchronizeDescription() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setDescription("test-description");
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("test-description", design.attr("description"));
    }

    public void testSynchronizeComponentError() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setComponentError(new UserError("<div>test-error</div>",
                ContentMode.HTML, ErrorLevel.ERROR));
        component.writeDesign(design, ctx);
        // we only changed one of the attributes, others are at default values
        assertEquals(1, design.attributes().size());
        assertEquals("<div>test-error</div>", design.attr("error"));
    }

    public void testSynchronizeSizeFull() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setSizeFull();
        component.writeDesign(design, ctx);
        // there should be only size full
        assertEquals(1, design.attributes().size());
        assertEquals("true", design.attr("size-full"));
    }

    public void testSynchronizeSizeAuto() {
        Element design = createDesign();
        AbstractComponent component = getPanel();
        component.setSizeUndefined();
        component.writeDesign(design, ctx);
        // there should be only size auto
        assertEquals(1, design.attributes().size());
        assertEquals("true", design.attr("size-auto"));
    }

    public void testSynchronizeHeightFull() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setHeight("100%");
        component.setWidth("20px");
        component.writeDesign(design, ctx);
        assertEquals("true", design.attr("height-full"));
    }

    public void testSynchronizeHeightAuto() {
        Element design = createDesign();
        // we need to have default height of 100% -> use split panel
        AbstractComponent component = getPanel();
        component.setHeight(null);
        component.setWidth("20px");
        component.writeDesign(design, ctx);
        assertEquals("true", design.attr("height-auto"));
    }

    public void testSynchronizeWidthFull() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setHeight("20px");
        component.setWidth("100%");
        component.writeDesign(design, ctx);
        assertEquals("true", design.attr("width-full"));
    }

    public void testSynchronizeWidthAuto() {
        Element design = createDesign();
        // need to get label, otherwise the default would be auto
        AbstractComponent component = getPanel();
        component.setHeight("20px");
        component.setWidth(null);
        component.writeDesign(design, ctx);
        assertEquals("true", design.attr("width-auto"));
    }

    public void testSynchronizeWidth() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setHeight("20px");
        component.setWidth("70%");
        component.writeDesign(design, ctx);
        assertEquals("70%", design.attr("width"));
    }

    public void testSynchronizeHeight() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        component.setHeight("20px");
        component.setWidth("70%");
        component.writeDesign(design, ctx);
        assertEquals("20px", design.attr("height"));
    }

    public void testSynchronizeResponsive() {
        Element design = createDesign();
        AbstractComponent component = getComponent();
        Responsive.makeResponsive(component);
        component.writeDesign(design, ctx);
        assertTrue("Design attributes should have key 'responsive'", design
                .attributes().hasKey("responsive"));
        assertFalse("Responsive attribute should not be 'false'",
                design.attr("responsive").equalsIgnoreCase("false"));
    }

    private AbstractComponent getComponent() {
        Button button = new Button();
        button.setHtmlContentAllowed(true);
        return button;
    }

    private AbstractComponent getPanel() {
        return new HorizontalSplitPanel();
    }

    private Element createDesign() {
        Attributes attr = new Attributes();
        attr.put("should_be_removed", "foo");
        Element node = new Element(Tag.valueOf("v-button"), "", attr);
        Element child = new Element(Tag.valueOf("to-be-removed"), "foo", attr);
        node.appendChild(child);
        return node;
    }
}
