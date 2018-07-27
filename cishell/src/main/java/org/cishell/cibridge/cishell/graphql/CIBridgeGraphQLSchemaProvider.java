package org.cishell.cibridge.cishell.graphql;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;

import org.cishell.cibridge.core.CIBridge;
import org.cishell.cibridge.graphql.resolvers.Mutation;
import org.cishell.cibridge.graphql.resolvers.Query;
import org.cishell.cibridge.graphql.resolvers.Subscription;
import org.cishell.cibridge.graphql.scalars.Scalars;
import org.cishell.cibridge.graphql.schema.CIBridgeSchema;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLErrorHandler;
import graphql.servlet.GraphQLSchemaProvider;

public class CIBridgeGraphQLSchemaProvider implements GraphQLSchemaProvider,GraphQLErrorHandler {
	private CIBridge cibridge;
	private final GraphQLSchema schema;
	private final GraphQLSchema readOnlySchema;
	
	private final Query queryResolver;
	private final Mutation mutationResolver;
	private final Subscription subscriptionResolver;
	
	public CIBridgeGraphQLSchemaProvider(CIBridge cibridge) {
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
		GraphQLSchema gs = null;
		try {
         gs = SchemaParser.newParser()
        		.schemaString(CIBridgeSchema.schemaString)
                .resolvers(this.queryResolver, this.mutationResolver, this.subscriptionResolver)
                .scalars(Scalars.date,Scalars.File,Scalars.Value, Scalars.ID)
                .build()
                .makeExecutableSchema();
        }catch (Exception e) {
			e.printStackTrace();
		}
		
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

	@Override
	public GraphQLSchema getSchema(HandshakeRequest request) {
		return readOnlySchema;
	}

	@Override
	public List<GraphQLError> processErrors(List<GraphQLError> errors) {
		System.out.println("Exception!!!!!!!!!!!!!!!1");
		
		return errors;
	}
		
	
}
