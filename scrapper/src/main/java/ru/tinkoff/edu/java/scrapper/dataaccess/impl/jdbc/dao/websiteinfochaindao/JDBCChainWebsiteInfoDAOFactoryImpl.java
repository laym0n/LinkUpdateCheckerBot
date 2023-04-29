package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.websiteinfochaindao;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.dataaccess.impl.jdbc.dao.*;

import javax.sql.DataSource;

@AllArgsConstructor
@Component
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JDBCChainWebsiteInfoDAOFactoryImpl implements JDBCChainWebsiteInfoDAOFactory {
    private JDBCGitHubInfoDAO gitHubInfoDAO;
    private JDBCStackOverflowInfoDAO stackOverflowInfoDAO;

    @Override
    public JDBCChainWebsiteInfoDAO getJDBCWebsiteInfoInfoDAO() {
        JDBCChainGitHubInfoDAOImpl chainGitHubInfoDAO = new JDBCChainGitHubInfoDAOImpl(gitHubInfoDAO, null);

        JDBCChainStackOverflowInfoDAOImpl chainStackOverflowInfoDAO =
                new JDBCChainStackOverflowInfoDAOImpl(stackOverflowInfoDAO,
                chainGitHubInfoDAO);
        return chainStackOverflowInfoDAO;
    }
}
