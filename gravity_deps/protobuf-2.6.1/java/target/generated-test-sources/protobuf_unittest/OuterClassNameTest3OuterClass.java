// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/google/protobuf/outer_class_name_test3.proto

package protobuf_unittest;

public final class OuterClassNameTest3OuterClass {
  private OuterClassNameTest3OuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TestMessage3OrBuilder extends
      // @@protoc_insertion_point(interface_extends:protobuf_unittest.TestMessage3)
      com.google.protobuf.MessageOrBuilder {
  }
  /**
   * Protobuf type {@code protobuf_unittest.TestMessage3}
   */
  public static final class TestMessage3 extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:protobuf_unittest.TestMessage3)
      TestMessage3OrBuilder {
    // Use TestMessage3.newBuilder() to construct.
    private TestMessage3(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private TestMessage3(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final TestMessage3 defaultInstance;
    public static TestMessage3 getDefaultInstance() {
      return defaultInstance;
    }

    public TestMessage3 getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private TestMessage3(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.class, protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.Builder.class);
    }

    public static com.google.protobuf.Parser<TestMessage3> PARSER =
        new com.google.protobuf.AbstractParser<TestMessage3>() {
      public TestMessage3 parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new TestMessage3(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<TestMessage3> getParserForType() {
      return PARSER;
    }

    public interface NestedMessageOrBuilder extends
        // @@protoc_insertion_point(interface_extends:protobuf_unittest.TestMessage3.NestedMessage)
        com.google.protobuf.MessageOrBuilder {
    }
    /**
     * Protobuf type {@code protobuf_unittest.TestMessage3.NestedMessage}
     */
    public static final class NestedMessage extends
        com.google.protobuf.GeneratedMessage implements
        // @@protoc_insertion_point(message_implements:protobuf_unittest.TestMessage3.NestedMessage)
        NestedMessageOrBuilder {
      // Use NestedMessage.newBuilder() to construct.
      private NestedMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private NestedMessage(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final NestedMessage defaultInstance;
      public static NestedMessage getDefaultInstance() {
        return defaultInstance;
      }

      public NestedMessage getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private NestedMessage(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_NestedMessage_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_NestedMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.class, protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.Builder.class);
      }

      public static com.google.protobuf.Parser<NestedMessage> PARSER =
          new com.google.protobuf.AbstractParser<NestedMessage>() {
        public NestedMessage parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new NestedMessage(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<NestedMessage> getParserForType() {
        return PARSER;
      }

      /**
       * Protobuf enum {@code protobuf_unittest.TestMessage3.NestedMessage.OuterClassNameTest3}
       *
       * <pre>
       * This enum's name is the same with the default outer class name of this
       * proto file. It's used to test if the compiler can avoid this conflict
       * correctly.
       * </pre>
       */
      public enum OuterClassNameTest3
          implements com.google.protobuf.ProtocolMessageEnum {
        /**
         * <code>DUMMY_VALUE = 1;</code>
         */
        DUMMY_VALUE(0, 1),
        ;

        /**
         * <code>DUMMY_VALUE = 1;</code>
         */
        public static final int DUMMY_VALUE_VALUE = 1;


        public final int getNumber() { return value; }

        public static OuterClassNameTest3 valueOf(int value) {
          switch (value) {
            case 1: return DUMMY_VALUE;
            default: return null;
          }
        }

        public static com.google.protobuf.Internal.EnumLiteMap<OuterClassNameTest3>
            internalGetValueMap() {
          return internalValueMap;
        }
        private static com.google.protobuf.Internal.EnumLiteMap<OuterClassNameTest3>
            internalValueMap =
              new com.google.protobuf.Internal.EnumLiteMap<OuterClassNameTest3>() {
                public OuterClassNameTest3 findValueByNumber(int number) {
                  return OuterClassNameTest3.valueOf(number);
                }
              };

        public final com.google.protobuf.Descriptors.EnumValueDescriptor
            getValueDescriptor() {
          return getDescriptor().getValues().get(index);
        }
        public final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptorForType() {
          return getDescriptor();
        }
        public static final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptor() {
          return protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.getDescriptor().getEnumTypes().get(0);
        }

        private static final OuterClassNameTest3[] VALUES = values();

        public static OuterClassNameTest3 valueOf(
            com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
          if (desc.getType() != getDescriptor()) {
            throw new java.lang.IllegalArgumentException(
              "EnumValueDescriptor is not for this type.");
          }
          return VALUES[desc.getIndex()];
        }

        private final int index;
        private final int value;

        private OuterClassNameTest3(int index, int value) {
          this.index = index;
          this.value = value;
        }

        // @@protoc_insertion_point(enum_scope:protobuf_unittest.TestMessage3.NestedMessage.OuterClassNameTest3)
      }

      private void initFields() {
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) return true;
        if (isInitialized == 0) return false;

        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }
      /**
       * Protobuf type {@code protobuf_unittest.TestMessage3.NestedMessage}
       */
      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder> implements
          // @@protoc_insertion_point(builder_implements:protobuf_unittest.TestMessage3.NestedMessage)
          protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessageOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_NestedMessage_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_NestedMessage_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.class, protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.Builder.class);
        }

        // Construct using protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.newBuilder()
        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_NestedMessage_descriptor;
        }

        public protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage getDefaultInstanceForType() {
          return protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.getDefaultInstance();
        }

        public protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage build() {
          protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage buildPartial() {
          protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage result = new protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage(this);
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage) {
            return mergeFrom((protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage other) {
          if (other == protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage.getDefaultInstance()) return this;
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.NestedMessage) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }

        // @@protoc_insertion_point(builder_scope:protobuf_unittest.TestMessage3.NestedMessage)
      }

      static {
        defaultInstance = new NestedMessage(true);
        defaultInstance.initFields();
      }

      // @@protoc_insertion_point(class_scope:protobuf_unittest.TestMessage3.NestedMessage)
    }

    private void initFields() {
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code protobuf_unittest.TestMessage3}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:protobuf_unittest.TestMessage3)
        protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3OrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.class, protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.Builder.class);
      }

      // Construct using protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return protobuf_unittest.OuterClassNameTest3OuterClass.internal_static_protobuf_unittest_TestMessage3_descriptor;
      }

      public protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 getDefaultInstanceForType() {
        return protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.getDefaultInstance();
      }

      public protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 build() {
        protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 buildPartial() {
        protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 result = new protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3(this);
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3) {
          return mergeFrom((protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 other) {
        if (other == protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3.getDefaultInstance()) return this;
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3 parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (protobuf_unittest.OuterClassNameTest3OuterClass.TestMessage3) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      // @@protoc_insertion_point(builder_scope:protobuf_unittest.TestMessage3)
    }

    static {
      defaultInstance = new TestMessage3(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:protobuf_unittest.TestMessage3)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_protobuf_unittest_TestMessage3_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_protobuf_unittest_TestMessage3_fieldAccessorTable;
  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_protobuf_unittest_TestMessage3_NestedMessage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_protobuf_unittest_TestMessage3_NestedMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n0com/google/protobuf/outer_class_name_t" +
      "est3.proto\022\021protobuf_unittest\"G\n\014TestMes" +
      "sage3\0327\n\rNestedMessage\"&\n\023OuterClassName" +
      "Test3\022\017\n\013DUMMY_VALUE\020\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_protobuf_unittest_TestMessage3_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_protobuf_unittest_TestMessage3_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_protobuf_unittest_TestMessage3_descriptor,
        new java.lang.String[] { });
    internal_static_protobuf_unittest_TestMessage3_NestedMessage_descriptor =
      internal_static_protobuf_unittest_TestMessage3_descriptor.getNestedTypes().get(0);
    internal_static_protobuf_unittest_TestMessage3_NestedMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_protobuf_unittest_TestMessage3_NestedMessage_descriptor,
        new java.lang.String[] { });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
