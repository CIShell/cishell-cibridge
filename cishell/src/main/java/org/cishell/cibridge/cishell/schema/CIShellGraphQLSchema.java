package org.cishell.cibridge.cishell.schema;

import org.cishell.cibridge.cishell.resolvers.Subscription;
import org.cishell.cibridge.graphql.Scalars;
import org.cishell.cibridge.cishell.data.AlgorithmDefinationCIShell;
import org.cishell.cibridge.cishell.resolvers.Mutation;
import org.cishell.cibridge.cishell.resolvers.Query;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;

public class CIShellGraphQLSchema {

	public static GraphQLSchema buildSchema() {
        GraphQLSchema gs = SchemaParser.newParser()
                .file("cibridge-schema_0.1.0-draft.graphqls") //parse the schema file created earlier
                .resolvers(new Query(new AlgorithmDefinationCIShell()),new Mutation(new AlgorithmDefinationCIShell()),new Subscription(new AlgorithmDefinationCIShell()))
                .scalars(Scalars.date,Scalars.File,Scalars.Value)
                .build()
                .makeExecutableSchema();
        return gs;
    }
}
