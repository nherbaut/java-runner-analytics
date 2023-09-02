package fr.pantheonsorbonne.ufr27.miage.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassBlackList {
    final public static Collection<Pattern> forbiddenPatterns;



    static {
        final Collection<String> forbiddenClasses = new ArrayList<>();
        forbiddenClasses.add("com.sun.jarsigner.*");
        forbiddenClasses.add("com.sun.java.accessibility.util.*");
        forbiddenClasses.add("com.sun.jdi.*");
        forbiddenClasses.add("com.sun.jdi.connect.*");
        forbiddenClasses.add("com.sun.jdi.connect.spi.*");
        forbiddenClasses.add("com.sun.jdi.event.*");
        forbiddenClasses.add("com.sun.jdi.request.*");
        forbiddenClasses.add("com.sun.management.*");
        forbiddenClasses.add("com.sun.net.httpserver.*");
        forbiddenClasses.add("com.sun.net.httpserver.spi.*");
        forbiddenClasses.add("com.sun.nio.sctp.*");
        forbiddenClasses.add("com.sun.security.auth.*");
        forbiddenClasses.add("com.sun.security.auth.callback.*");
        forbiddenClasses.add("com.sun.security.auth.login.*");
        forbiddenClasses.add("com.sun.security.auth.module.*");
        forbiddenClasses.add("com.sun.security.jgss.*");
        forbiddenClasses.add("com.sun.source.doctree.*");
        forbiddenClasses.add("com.sun.source.tree.*");
        forbiddenClasses.add("com.sun.source.util.*");
        forbiddenClasses.add("com.sun.tools.attach.*");
        forbiddenClasses.add("com.sun.tools.attach.spi.*");
        forbiddenClasses.add("com.sun.tools.javac.*");
        forbiddenClasses.add("com.sun.tools.jconsole.*");
        forbiddenClasses.add("java.applet.*");
        forbiddenClasses.add("java.awt.*");
        forbiddenClasses.add("java.awt.color.*");
        forbiddenClasses.add("java.awt.datatransfer.*");
        forbiddenClasses.add("java.awt.desktop.*");
        forbiddenClasses.add("java.awt.dnd.*");
        forbiddenClasses.add("java.awt.event.*");
        forbiddenClasses.add("java.awt.font.*");
        forbiddenClasses.add("java.awt.geom.*");
        forbiddenClasses.add("java.awt.im.*");
        forbiddenClasses.add("java.awt.im.spi.*");
        forbiddenClasses.add("java.awt.image.*");
        forbiddenClasses.add("java.awt.image.renderable.*");
        forbiddenClasses.add("java.awt.print.*");
        forbiddenClasses.add("java.beans.*");
        forbiddenClasses.add("java.beans.beancontext.*");
// forbiddenClasses.add("java.io.*");
// forbiddenClasses.add("java.lang.*");
// forbiddenClasses.add("java.lang.annotation.*");
// forbiddenClasses.add("java.lang.constant.*");
// forbiddenClasses.add("java.lang.instrument.*");
//forbiddenClasses.add("java.lang.invoke.*");
// forbiddenClasses.add("java.lang.management.*");
// forbiddenClasses.add("java.lang.module.*");
// forbiddenClasses.add("java.lang.ref.*");
// forbiddenClasses.add("java.lang.reflect.*");
// forbiddenClasses.add("java.lang.runtime.*");
// forbiddenClasses.add("java.math.*");
        forbiddenClasses.add("java.net.*");
        forbiddenClasses.add("java.net.http.*");
        forbiddenClasses.add("java.net.spi.*");
        forbiddenClasses.add("java.nio.*");
        forbiddenClasses.add("java.nio.channels.*");
        forbiddenClasses.add("java.nio.channels.spi.*");
        forbiddenClasses.add("java.nio.charset.*");
        forbiddenClasses.add("java.nio.charset.spi.*");
        forbiddenClasses.add("java.nio.file.*");
        forbiddenClasses.add("java.nio.file.attribute.*");
        forbiddenClasses.add("java.nio.file.spi.*");
        forbiddenClasses.add("java.rmi.*");
        forbiddenClasses.add("java.rmi.dgc.*");
        forbiddenClasses.add("java.rmi.registry.*");
        forbiddenClasses.add("java.rmi.server.*");
        forbiddenClasses.add("java.security.*");
        forbiddenClasses.add("java.security.cert.*");
        forbiddenClasses.add("java.security.interfaces.*");
        forbiddenClasses.add("java.security.spec.*");
        forbiddenClasses.add("java.sql.*");
        //forbiddenClasses.add("java.text.*");
        forbiddenClasses.add("java.text.spi.*");
// forbiddenClasses.add("java.time.*");
        //forbiddenClasses.add("java.time.chrono.*");
        //forbiddenClasses.add("java.time.format.*");
        //forbiddenClasses.add("java.time.temporal.*");
        //forbiddenClasses.add("java.time.zone.*");
// forbiddenClasses.add("java.util.*");
// forbiddenClasses.add("java.util.concurrent.*");
// forbiddenClasses.add("java.util.concurrent.atomic.*");
// forbiddenClasses.add("java.util.concurrent.locks.*");
// forbiddenClasses.add("java.util.function.*");
// forbiddenClasses.add("java.util.jar.*");
// forbiddenClasses.add("java.util.logging.*");
// forbiddenClasses.add("java.util.prefs.*");
// forbiddenClasses.add("java.util.random.*");
// forbiddenClasses.add("java.util.regex.*");
// forbiddenClasses.add("java.util.spi.*");
// forbiddenClasses.add("java.util.stream.*");
        forbiddenClasses.add("java.util.zip.*");
        forbiddenClasses.add("javax.accessibility.*");
// forbiddenClasses.add("javax.annotation.processing.*");
// forbiddenClasses.add("javax.crypto.*");
// forbiddenClasses.add("javax.crypto.interfaces.*");
// forbiddenClasses.add("javax.crypto.spec.*");
        forbiddenClasses.add("javax.imageio.*");
        forbiddenClasses.add("javax.imageio.event.*");
        forbiddenClasses.add("javax.imageio.metadata.*");
        forbiddenClasses.add("javax.imageio.plugins.bmp.*");
        forbiddenClasses.add("javax.imageio.plugins.jpeg.*");
        forbiddenClasses.add("javax.imageio.plugins.tiff.*");
        forbiddenClasses.add("javax.imageio.spi.*");
        forbiddenClasses.add("javax.imageio.stream.*");
// forbiddenClasses.add("javax.lang.model.*");
// forbiddenClasses.add("javax.lang.model.element.*");
// forbiddenClasses.add("javax.lang.model.type.*");
// forbiddenClasses.add("javax.lang.model.util.*");
        forbiddenClasses.add("javax.management.*");
        forbiddenClasses.add("javax.management.loading.*");
        forbiddenClasses.add("javax.management.modelmbean.*");
        forbiddenClasses.add("javax.management.monitor.*");
        forbiddenClasses.add("javax.management.openmbean.*");
        forbiddenClasses.add("javax.management.relation.*");
        forbiddenClasses.add("javax.management.remote.*");
        forbiddenClasses.add("javax.management.remote.rmi.*");
        forbiddenClasses.add("javax.management.timer.*");
        forbiddenClasses.add("javax.naming.*");
        forbiddenClasses.add("javax.naming.directory.*");
        forbiddenClasses.add("javax.naming.event.*");
        forbiddenClasses.add("javax.naming.ldap.*");
        forbiddenClasses.add("javax.naming.ldap.spi.*");
        forbiddenClasses.add("javax.naming.spi.*");
        forbiddenClasses.add("javax.net.*");
        forbiddenClasses.add("javax.net.ssl.*");
        forbiddenClasses.add("javax.print.*");
        forbiddenClasses.add("javax.print.attribute.*");
        forbiddenClasses.add("javax.print.attribute.standard.*");
        forbiddenClasses.add("javax.print.event.*");
        forbiddenClasses.add("javax.rmi.ssl.*");
        forbiddenClasses.add("javax.script.*");
        forbiddenClasses.add("javax.security.auth.*");
        forbiddenClasses.add("javax.security.auth.callback.*");
        forbiddenClasses.add("javax.security.auth.kerberos.*");
        forbiddenClasses.add("javax.security.auth.login.*");
        forbiddenClasses.add("javax.security.auth.spi.*");
        forbiddenClasses.add("javax.security.auth.x500.*");
        forbiddenClasses.add("javax.security.cert.*");
        forbiddenClasses.add("javax.security.sasl.*");
        forbiddenClasses.add("javax.smartcardio.*");
        forbiddenClasses.add("javax.sound.midi.*");
        forbiddenClasses.add("javax.sound.midi.spi.*");
        forbiddenClasses.add("javax.sound.sampled.*");
        forbiddenClasses.add("javax.sound.sampled.spi.*");
        forbiddenClasses.add("javax.sql.*");
        forbiddenClasses.add("javax.sql.rowset.*");
        forbiddenClasses.add("javax.sql.rowset.serial.*");
        forbiddenClasses.add("javax.sql.rowset.spi.*");
        forbiddenClasses.add("javax.swing.*");
        forbiddenClasses.add("javax.swing.border.*");
        forbiddenClasses.add("javax.swing.colorchooser.*");
        forbiddenClasses.add("javax.swing.event.*");
        forbiddenClasses.add("javax.swing.filechooser.*");
        forbiddenClasses.add("javax.swing.plaf.*");
        forbiddenClasses.add("javax.swing.plaf.basic.*");
        forbiddenClasses.add("javax.swing.plaf.metal.*");
        forbiddenClasses.add("javax.swing.plaf.multi.*");
        forbiddenClasses.add("javax.swing.plaf.nimbus.*");
        forbiddenClasses.add("javax.swing.plaf.synth.*");
        forbiddenClasses.add("javax.swing.table.*");
        forbiddenClasses.add("javax.swing.text.*");
        forbiddenClasses.add("javax.swing.text.html.*");
        forbiddenClasses.add("javax.swing.text.html.parser.*");
        forbiddenClasses.add("javax.swing.text.rtf.*");
        forbiddenClasses.add("javax.swing.tree.*");
        forbiddenClasses.add("javax.swing.undo.*");
        forbiddenClasses.add("javax.tools.*");
        forbiddenClasses.add("javax.transaction.xa.*");
// forbiddenClasses.add("javax.xml.*");
// forbiddenClasses.add("javax.xml.catalog.*");
// forbiddenClasses.add("javax.xml.crypto.*");
// forbiddenClasses.add("javax.xml.crypto.dom.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.dom.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.keyinfo.*");
// forbiddenClasses.add("javax.xml.crypto.dsig.spec.*");
// forbiddenClasses.add("javax.xml.datatype.*");
// forbiddenClasses.add("javax.xml.namespace.*");
// forbiddenClasses.add("javax.xml.parsers.*");
// forbiddenClasses.add("javax.xml.stream.*");
// forbiddenClasses.add("javax.xml.stream.events.*");
// forbiddenClasses.add("javax.xml.stream.util.*");
// forbiddenClasses.add("javax.xml.transform.*");
// forbiddenClasses.add("javax.xml.transform.dom.*");
// forbiddenClasses.add("javax.xml.transform.sax.*");
// forbiddenClasses.add("javax.xml.transform.stax.*");
// forbiddenClasses.add("javax.xml.transform.stream.*");
// forbiddenClasses.add("javax.xml.validation.*");
// forbiddenClasses.add("javax.xml.xpath.*");
        forbiddenClasses.add("jdk.dynalink.*");
        forbiddenClasses.add("jdk.dynalink.beans.*");
        forbiddenClasses.add("jdk.dynalink.linker.*");
        forbiddenClasses.add("jdk.dynalink.linker.support.*");
        forbiddenClasses.add("jdk.dynalink.support.*");
        forbiddenClasses.add("jdk.incubator.foreign.*");
        forbiddenClasses.add("jdk.incubator.vector.*");
        forbiddenClasses.add("jdk.javadoc.doclet.*");
        forbiddenClasses.add("jdk.jfr.*");
        forbiddenClasses.add("jdk.jfr.consumer.*");
        forbiddenClasses.add("jdk.jshell.*");
        forbiddenClasses.add("jdk.jshell.execution.*");
        forbiddenClasses.add("jdk.jshell.spi.*");
        forbiddenClasses.add("jdk.jshell.tool.*");
        forbiddenClasses.add("jdk.management.jfr.*");
        forbiddenClasses.add("jdk.net.*");
        forbiddenClasses.add("jdk.nio.*");
        forbiddenClasses.add("jdk.nio.mapmode.*");
        forbiddenClasses.add("jdk.security.jarsigner.*");
        forbiddenClasses.add("netscape.javascript.*");
// forbiddenClasses.add("org.ietf.jgss.*");
// forbiddenClasses.add("org.w3c.dom.*");
// forbiddenClasses.add("org.w3c.dom.bootstrap.*");
// forbiddenClasses.add("org.w3c.dom.css.*");
// forbiddenClasses.add("org.w3c.dom.events.*");
// forbiddenClasses.add("org.w3c.dom.html.*");
// forbiddenClasses.add("org.w3c.dom.ls.*");
// forbiddenClasses.add("org.w3c.dom.ranges.*");
// forbiddenClasses.add("org.w3c.dom.stylesheets.*");
// forbiddenClasses.add("org.w3c.dom.traversal.*");
// forbiddenClasses.add("org.w3c.dom.views.*");
// forbiddenClasses.add("org.w3c.dom.xpath.*");
// forbiddenClasses.add("org.xml.sax.*");
// forbiddenClasses.add("org.xml.sax.ext.*");
// forbiddenClasses.add("org.xml.sax.helpers .*");

        forbiddenClasses.add("java.io.FileWriter");
        forbiddenClasses.add("java.io.FileOutputStream");
        forbiddenClasses.add("java.io.FileReader");
        forbiddenClasses.add("java.io.FileInputStream");
        forbiddenClasses.add("java.nio.file.*");
        forbiddenClasses.add("java.lang.Thread");

        forbiddenClasses.add("java.lang.instrument.*");
        //forbiddenClasses.add("java.lang.invoke.*");

        forbiddenClasses.add("java.lang.ref.*");

        forbiddenClasses.add("java.lang.reflect.*");
        forbiddenClasses.add("java.lang.runtime.*");
        forbiddenClasses.add("java.lang.module.*");
        forbiddenClasses.add("java.lang.management.*");
        forbiddenClasses.add("sun.misc.Unsafe");


        forbiddenPatterns = forbiddenClasses.stream().map(s -> Pattern.compile(s)).collect(Collectors.toList());
    }
}