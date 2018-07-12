package org.cishell.cibridge.cishellOSGI;

import org.cishell.cibridge.model.Scalars;
import org.cishell.cibridge.resolvers.AlgorithmDefination_OSGI_mock;
import org.cishell.cibridge.resolvers.Query;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;

public class CIShellGraphQLSchema {
	public static GraphQLSchema buildSchema() {
        GraphQLSchema gs = SchemaParser.newParser()
                .file("cibridge-schema_0.1.0-draft.graphqls") //parse the schema file created earlier
                .resolvers(new Query(new AlgorithmDefination_OSGI_mock()))
                .scalars(Scalars.dateTime)
                .build()
                .makeExecutableSchema();
        return gs;
    }
}
