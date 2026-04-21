package lab6.common.util;

import java.io.*;

/**
 * Provides utility methods for object serialization and deserialization.
 *
 * <p>
 * This class contains static methods for converting serializable objects
 * to byte arrays and restoring objects from byte arrays.
 */
public class SerializationUtils {
  /**
   * Serializes a {@code Serializable} object into a byte array.
   *
   * @param obj the object to serialize
   * @return a byte array containing the serialized form of the object
   * @throws IOException if an I/O error occurs during serialization
   */
  public static byte[] serialize(Serializable obj) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(obj);
    objectOutputStream.flush();
    return byteArrayOutputStream.toByteArray();
  }

  /**
   * Deserializes an object from a byte array.
   *
   * @param <T>   the expected type of the deserialized object
   * @param data  the byte array containing the serialized object
   * @param clazz the class of the expected result type
   * @return the deserialized object cast to the specified type
   * @throws IOException            if an I/O error occurs during deserialization
   * @throws ClassNotFoundException if the class of the serialized object cannot
   *                                be found
   * @throws ClassCastException     if the deserialized object cannot be cast to
   *                                the expected type
   */
  public static <T> T deserialize(byte[] data, Class<T> clazz) throws IOException, ClassNotFoundException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    Object obj = objectInputStream.readObject();
    return clazz.cast(obj);
  }
}
