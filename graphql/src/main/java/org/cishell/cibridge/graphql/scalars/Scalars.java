package org.cishell.cibridge.graphql.scalars;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

public class Scalars  {
    
    public static GraphQLScalarType date = new GraphQLScalarType("Date", "Date scalar", new Coercing() {
        public String serialize(Object input) {
            //serialize the ZonedDateTime into string on the way out
            return ((ZonedDateTime)input).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        public Object parseValue(Object input) {
            return serialize(input);
        }

        public ZonedDateTime parseLiteral(Object input) {
            //parse the string values coming in
            if (input instanceof StringValue) {
                return ZonedDateTime.parse(((StringValue) input).getValue());
            } else {
                return null;
            }
        }
    });
    
    public static GraphQLScalarType File = new GraphQLScalarType("File", "File scalar", new Coercing() {
        public String serialize(Object input) {
            return input.toString();
        }

        public Object parseValue(Object input) {
            return serialize(input);
        }

        public String parseLiteral(Object input) {
            //parse the string values coming in
            if (input instanceof StringValue) {
                return (String) ((StringValue) input).getValue();
            } else {
                return null;
            }
        }
    });

    public static GraphQLScalarType Value = new GraphQLScalarType("Value", "Value scalar", new Coercing() {
        public String serialize(Object input) {
            return input.toString();
        }

        public Object parseValue(Object input) {
            return serialize(input);
        }

        public String parseLiteral(Object input) {
            //parse the string values coming in
        	try {
            if (input instanceof StringValue) {
                return (String) ((StringValue) input).getValue();
            } else {
                return null;
            }}catch(Exception e) {
            	e.printStackTrace();
            }
        	return null;
        }
    });
 
    
    public static GraphQLScalarType ID = new GraphQLScalarType("ID", "ID scalar", new Coercing() {
        public String serialize(Object input) {
            return input.toString();
        }

        public Object parseValue(Object input) {
            return serialize(input);
        }

        public String parseLiteral(Object input) {
            //parse the string values coming in
            if (input instanceof StringValue) {
                return (String) ((StringValue) input).getValue();
            } else {
                return null;
            }
        }
    });


}
