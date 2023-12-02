package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.ColorList;
import models.Colors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ColorInt {

    private static List<Colors> colorList;

    public ColorInt () throws IOException {
        fetchColors();
    }

    public void fetchColors() throws IOException {
        File file = new File(
                Objects.requireNonNull(this.getClass().getClassLoader().getResource("colors.json")).getFile()
        );
        ObjectMapper mapper = new ObjectMapper();

        // Read the JSON data into a List<Colors>
        colorList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Colors.class));
    }


    public String getColor(int i) {
        return colorList.get((i % colorList.size())).getColor_code();
    }
}
