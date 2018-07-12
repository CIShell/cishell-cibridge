package org.cishell.cibridge.cishell.schema;

import org.cishell.cibridge.mock.resolvers.Mutation;
import org.cishell.cibridge.model.Scalars;
import org.cishell.cibridge.mock.data.AlgorithmDefinationMock;
import org.cishell.cibridge.mock.resolvers.Query;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;

public class CIShellGraphQLSchema {
	public static GraphQLSchema buildSchema() {
        GraphQLSchema gs = SchemaParser.newParser()
                .file("cibridge-schema_0.1.0-draft.graphqls") //parse the schema file created earlier
                .resolvers(new Query(new AlgorithmDefinationMock()))
                .scalars(Scalars.date)
                .build()
                .makeExecutableSchema();
        return gs;
    }
}
