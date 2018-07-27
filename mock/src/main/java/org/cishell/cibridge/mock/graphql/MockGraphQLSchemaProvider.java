package org.cishell.cibridge.mock.graphql;

import javax.servlet.http.HttpServletRequest;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.graphql.resolvers.Mutation;
import org.cishell.cibridge.graphql.resolvers.Query;
import org.cishell.cibridge.graphql.resolvers.Subscription;
import org.cishell.cibridge.graphql.scalars.Scalars;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLSchemaProvider;

public class MockGraphQLSchemaProvider implements GraphQLSchemaProvider {
	private CIBridge cibridge;
	private final GraphQLSchema schema;
	private final GraphQLSchema readOnlySchema;
	
	private final Query queryResolver;
	private final Mutation mutationResolver;
	private final Subscription subscriptionResolver;
	
	public MockGraphQLSchemaProvider(CIBridge cibridge) {
		this.cibridge = cibridge;
		this.queryResolver = new Query(cibridge);
		this.mutationResolver = new Mutation(cibridge);
		this.subscriptionResolver = new Subscription(cibridge);
		this.schema = this.buildSchema();
		this.readOnlySchema = GraphQLSchemaProvider.copyReadOnly(this.schema);
	}
	
	public void setCIBridge(CIBridge cibridge) {
		this.cibridge = cibridge;
		this.queryResolver.setCIBridge(cibridge);
		this.mutationResolver.setCIBridge(cibridge);
		this.subscriptionResolver.setCIBridge(cibridge);
	}
	public CIBridge getCIBridge() {
		return this.cibridge;
	}

	private GraphQLSchema buildSchema() {
        GraphQLSchema gs = SchemaParser.newParser()
//        		.schemaString(CIBridgeSchema.schemaString)
        		.file("cibridge-schema_0.1.0-draft.graphqls")
                .resolvers(this.queryResolver, this.mutationResolver, this.subscriptionResolver)
                .scalars(Scalars.date,Scalars.File,Scalars.Value)
                .build()
                .makeExecutableSchema();
        return gs;
    }

	@Override
	public GraphQLSchema getSchema(HttpServletRequest request) {
		return this.getSchema();
	}

	@Override
	public GraphQLSchema getSchema() {
		return this.schema;
	}

	@Override
	public GraphQLSchema getReadOnlySchema(HttpServletRequest request) {
		return readOnlySchema;
	}
}
