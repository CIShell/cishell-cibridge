package org.cishell.cibridge;

import javax.servlet.annotation.WebServlet;

import org.cishell.cibridge.model.Query;
import org.cishell.cibridge.resolvers.AlgorithmDefination_OSGI_mock;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;


@WebServlet(urlPatterns = "/graphql")
public class CIShellCIBridgeSchemaEndpoint extends SimpleGraphQLServlet {

	public CIShellCIBridgeSchemaEndpoint() {
		super(buildSchema());
	}
    public static GraphQLSchema buildSchema() {
    	AlgorithmDefination_OSGI_mock algorithmDefination_OSGI_mock = new AlgorithmDefination_OSGI_mock();
        return SchemaParser.newParser()
                .file("cibridge-schema_0.1.0-draft.graphqls") //parse the schema file created earlier
                .resolvers((GraphQLResolver)new Query(algorithmDefination_OSGI_mock))
                .build()
                .makeExecutableSchema();
    }
}