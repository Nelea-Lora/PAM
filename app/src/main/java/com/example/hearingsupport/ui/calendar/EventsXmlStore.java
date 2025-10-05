package com.example.hearingsupport.ui.calendar;

import android.content.Context;

import com.example.hearingsupport.ui.calendar.Event;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventsXmlStore {

    private static final String FILE_NAME = "events.xml";

    private static File getFile(Context context) {
        return new File(context.getFilesDir(), FILE_NAME);
    }

    // Добавить новое событие
    public static boolean add(Context context, Event event) {
        try {
            List<Event> events = getAll(context);
            events.add(event);
            saveAll(context, events);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получить все события
    public static List<Event> getAll(Context context) {
        List<Event> result = new ArrayList<>();
        File file = getFile(context);
        if (!file.exists()) return result;

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(file));
            NodeList list = doc.getElementsByTagName("event");

            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);
                String id = el.getAttribute("id");
                LocalDate date = LocalDate.parse(el.getElementsByTagName("date").item(0).getTextContent());
                String title = el.getElementsByTagName("title").item(0).getTextContent();
                String info = el.getElementsByTagName("info").item(0).getTextContent();
                result.add(new Event(id, date, title, info));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Сохранить все события в файл
    private static void saveAll(Context context, List<Event> events) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("events");
        doc.appendChild(root);

        for (Event e : events) {
            Element ev = doc.createElement("event");
            ev.setAttribute("id", e.getId());

            Element date = doc.createElement("date");
            date.setTextContent(e.getDate().toString());
            ev.appendChild(date);

            Element title = doc.createElement("title");
            title.setTextContent(e.getTitle());
            ev.appendChild(title);

            Element info = doc.createElement("info");
            info.setTextContent(e.getInfo() == null ? "" : e.getInfo());
            ev.appendChild(info);

            root.appendChild(ev);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(getFile(context))));
    }

    public static Event createNew(LocalDate date, String title, String info) {
        return new Event(UUID.randomUUID().toString(), date, title, info);
    }

    // Получить событие по ID
    public static Event getById(Context context, String id) {
        List<Event> events = getAll(context);
        for (Event e : events) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    // Обновить событие
    public static boolean update(Context context, Event updated) {
        try {
            List<Event> events = getAll(context);
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getId().equals(updated.getId())) {
                    events.set(i, updated);
                    saveAll(context, events);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
