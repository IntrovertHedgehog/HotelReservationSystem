/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumeration;

import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author Winter
 */
@Converter(autoApply = true)
public class BedSizeConverter implements AttributeConverter<BedSize, String> {
    @Override
    public String convertToDatabaseColumn(BedSize bedSize) {
        if (bedSize == null) {
            return null;
        }
        return bedSize.name();
    }

    @Override
    public BedSize convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(BedSize.values())
          .filter(b -> b.name().equals(name))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}
