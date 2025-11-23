package com.skupina1.location.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = false)
public class GeometryConverter implements AttributeConverter<Point, PGobject> {

    private static final WKTReader wktReader = new WKTReader();
    private static final WKTWriter wktWriter = new WKTWriter();

    @Override
    public PGobject convertToDatabaseColumn(Point point) {
        if (point == null) {
            return null;
        }

        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("geometry");

            // Write as EWKT
            String wkt = wktWriter.write(point);
            String ewkt = "SRID=" + point.getSRID() + ";" + wkt;

            pgObject.setValue(ewkt);
            return pgObject;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to convert Point to PGobject", e);
        }
    }

    @Override
    public Point convertToEntityAttribute(PGobject pgObject) {
        if (pgObject == null || pgObject.getValue() == null) {
            return null;
        }

        try {
            String value = pgObject.getValue();
            int srid = 0;
            String wkt = value;

            // Parse EWKT: "SRID=4326;POINT(14.505751 46.056946)"
            if (value.startsWith("SRID=")) {
                int semicolonIndex = value.indexOf(';');
                srid = Integer.parseInt(value.substring(5, semicolonIndex));
                wkt = value.substring(semicolonIndex + 1);
            }

            Geometry geometry = wktReader.read(wkt);

            if (!(geometry instanceof Point point)) {
                throw new IllegalArgumentException("Expected Point, got " + geometry.getGeometryType());
            }

            point.setSRID(srid);

            return point;
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse geometry: " + pgObject.getValue(), e);
        }
    }
}