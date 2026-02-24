package fr.univcotedazur.skimaster.customer.repositories;

import fr.univcotedazur.skimaster.customer.entities.Customer;
import fr.univcotedazur.skimaster.forfait.entities.AgeCategory;
import fr.univcotedazur.skimaster.forfait.entities.Forfait;
import fr.univcotedazur.skimaster.forfait.entities.ForfaitOffer;
import fr.univcotedazur.skimaster.forfait.entities.PassType;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitOfferRepository;
import fr.univcotedazur.skimaster.forfait.repositories.ForfaitRepository;
import fr.univcotedazur.skimaster.order.entities.Order;
import fr.univcotedazur.skimaster.order.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerAccountSchemaAndReadTest {

    @Autowired private DataSource dataSource;

    @Autowired private CustomerRepository customerRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private ForfaitOfferRepository forfaitOfferRepository;
    @Autowired private ForfaitRepository forfaitRepository;

    @Test
    void schemaContainsUs3TablesForeignKeysAndUsefulCustomerIndexes() throws Exception {
        try (var connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            Set<String> tables = listTables(metaData);
            assertThat(tables).anyMatch(t -> t.equals("customer") || t.equals("customers"));
            assertThat(tables).contains("orders");

            assertThat(tables).anyMatch(t -> t.equals("order_items") || t.equals("orders_items") || t.equals("product"));
            assertThat(tables).anyMatch(t -> t.equals("forfait") || t.equals("passes"));

            assertThat(listImportedKeyColumns(metaData, "orders")).contains("customer_id");
            assertThat(listImportedKeyColumns(metaData, "forfait")).contains("offer_id");
            assertThat(listImportedKeyColumns(metaData, "forfait")).doesNotContain("customer_id");
            assertThat(listColumns(metaData, "forfait")).contains("customer_id");

            assertThat(listIndexedColumns(metaData, "orders")).contains("customer_id");
            assertThat(listIndexedColumns(metaData, "forfait")).contains("customer_id");
        }
    }

    @Test
    void persistAndReadFixturesByCustomerId() {
        Customer clienta = customerRepository.saveAndFlush(new Customer("Clienta-us3", "1234567890"));

        Order order = new Order(clienta, Set.of(), 42.0);
        orderRepository.saveAndFlush(order);

        ForfaitOffer offer = forfaitOfferRepository.saveAndFlush(new ForfaitOffer(AgeCategory.ADULT, PassType.FULL, 35.0));
        forfaitRepository.saveAndFlush(new Forfait(clienta.getId(), LocalDate.parse("2026-01-01"), offer));

        List<Order> orders = orderRepository.findByCustomer_Id(clienta.getId());
        List<Forfait> forfaits = forfaitRepository.findAllByCustomerId(clienta.getId());

        assertThat(orders).hasSize(1);
        assertThat(orders.getFirst().getPrice()).isEqualTo(42.0);
        assertThat(forfaits).hasSize(1);
        assertThat(forfaits.getFirst().getPassType()).isEqualTo(PassType.FULL);
    }

    private Set<String> listTables(DatabaseMetaData metaData) throws Exception {
        try (ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
            Set<String> tables = new java.util.HashSet<>();
            while (resultSet.next()) {
                String table = resultSet.getString("TABLE_NAME");
                if (table != null) {
                    tables.add(table.toLowerCase(Locale.ROOT));
                }
            }
            return tables;
        }
    }

    private Set<String> listImportedKeyColumns(DatabaseMetaData metaData, String table) throws Exception {
        try (ResultSet resultSet = metaData.getImportedKeys(null, null, table.toUpperCase(Locale.ROOT))) {
            Set<String> fkColumns = new java.util.HashSet<>();
            while (resultSet.next()) {
                fkColumns.add(resultSet.getString("FKCOLUMN_NAME").toLowerCase(Locale.ROOT));
            }
            return fkColumns;
        }
    }

    private Set<String> listIndexedColumns(DatabaseMetaData metaData, String table) throws Exception {
        try (ResultSet resultSet = metaData.getIndexInfo(null, null, table.toUpperCase(Locale.ROOT), false, false)) {
            Set<String> indexedColumns = new java.util.HashSet<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                if (columnName != null) {
                    indexedColumns.add(columnName.toLowerCase(Locale.ROOT));
                }
            }
            return indexedColumns;
        }
    }

    private Set<String> listColumns(DatabaseMetaData metaData, String table) throws Exception {
        try (ResultSet resultSet = metaData.getColumns(null, null, table.toUpperCase(Locale.ROOT), "%")) {
            Set<String> columns = new java.util.HashSet<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                if (columnName != null) {
                    columns.add(columnName.toLowerCase(Locale.ROOT));
                }
            }
            return columns;
        }
    }
}
