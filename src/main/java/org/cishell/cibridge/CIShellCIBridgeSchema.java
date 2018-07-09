import com.coxautodev.graphql.tools.SchemaParser;
import javax.servlet.annotation.WebServlet;
import graphql.servlet.SimpleGraphQLServlet;


@WebServlet(urlPatterns = "/graphql")
public class CIShellCIBridgeSchema extends SimpleGraphQLServlet {

    public CIShellCIBridgeSchema() {
        super(SchemaParser.newParser()
                .file("cibridge-schema_0.1.0-draft.graphqls") //parse the schema file created earlier
                .build()
                .makeExecutableSchema());
    }
}