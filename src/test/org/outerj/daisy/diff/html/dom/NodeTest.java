package org.outerj.daisy.diff.html.dom;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Arrays;

/**
 * Tests (a tiny part of the) functionality of {@link Node}.
 */
public class NodeTest
{
    @Test
    public void testGetParentTree() throws Exception
    {
        TagNode root = new TagNode(null, "root", new AttributesImpl());
        TagNode intermediate = new TagNode(root, "middle", new AttributesImpl());
        root.addChild(intermediate);
        TagNode leaf = new TagNode(intermediate, "leaf", new AttributesImpl());
        intermediate.addChild(leaf);
        assertEquals(Arrays.asList(root, intermediate), leaf.getParentTree());
    }
}
