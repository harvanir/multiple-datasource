package org.harvanir.demo.datasource.advice;

import org.harvanir.demo.datasource.configuration.TransactionRoutingDataSourceConfiguration;
import org.harvanir.demo.datasource.support.DataSourceContextHolder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Harvan Irsyadi
 */
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@SpringBootTest(classes = {RoutingDataSourceAwareTransactionalAspectTestApp.class, TransactionRoutingDataSourceConfiguration.class, RoutingDataSourceAwareTransactionalAspectTest.Test4Configuration.class})
public class RoutingDataSourceAwareTransactionalAspectTest {

    @Autowired
    private Test1 test1;

    @Autowired
    private Test2 test2;

    @Autowired
    private Test3 test3;

    @Autowired
    private Test4 test4;

    @Autowired
    private Test5 test5;

    @Autowired
    private Test6 test6;

    @Test
    void test1_GivenLevelOneOfAlwaysNewAnnotatedMethod() {
        assertNull(DataSourceContextHolder.getRouteKey());
        test1.method1();
        assertNull(DataSourceContextHolder.getRouteKey());
    }

    @Test
    void test2_GivenLevelOneOfAlwaysNewAnnotatedMethod_WithLevelTwoOfAlwaysNewAnnotatedMethod_UsingSameRouteKey() {
        assertNull(DataSourceContextHolder.getRouteKey());
        test2.method1();
        assertNull(DataSourceContextHolder.getRouteKey());
    }

    @Test
    void test3_GivenLevelOneOfAlwaysNewAnnotatedMethod_WithLevelTwoRequiredAnnotatedMethod_UsingSameRouteKey() {
        assertNull(DataSourceContextHolder.getRouteKey());
        test3.method1();
        assertNull(DataSourceContextHolder.getRouteKey());
    }

    @Test
    void test4_GivenLevelOneOfAlwaysNewAnnotatedMethod_WithLevelTwoRequiredAnnotatedMethod_InTheDifferentKey() {
        assertNull(DataSourceContextHolder.getRouteKey());
        test4.method1();
        assertNull(DataSourceContextHolder.getRouteKey());
    }

    @Test
    void test5_GivenLevelOneAnnotatedParentMethod_WithTwoLevelTwoAnnotatedMethod_InTheSameKey() {
        assertNull(DataSourceContextHolder.getRouteKey());
        test5.method1();
        assertNull(DataSourceContextHolder.getRouteKey());
    }

    @Test
    void test6_GivenInvalidRouteKey_Failed() {
        assertThrows(InvalidRouteKeyException.class, () -> test6.method1());
        assertThrows(InvalidRouteKeyException.class, () -> test6.method2());
        assertThrows(InvalidRouteKeyException.class, () -> test6.method3());
        assertThrows(InvalidRouteKeyException.class, () -> test6.method4());
    }

    interface Test1 {

        void method1();
    }

    interface Test2 {

        void method1();

        void method2();
    }

    interface Test3 {

        void method1();

        void method2();
    }

    interface Test4 {

        void method1();

        void method2();
    }

    interface Test5 {

        void method1();
    }

    interface Test6 {

        void method1();

        void method2();

        void method3();

        void method4();
    }

    @TestConfiguration
    static class Test4Configuration {

        @Bean
        public Test1 test1() {
            return new Test1Impl();
        }

        @Bean
        public Test2 test2(@Lazy Test2 test2) {
            return new Test2Impl(test2);
        }

        @Bean
        public Test3 test3(@Lazy Test3 test3) {
            return new Test3Impl(test3);
        }

        @Bean
        public Test4 test4(@Lazy Test4 test4) {
            return new Test4Impl(test4);
        }

        @Bean
        public Test5 test5() {
            return new Test5Impl();
        }

        @Bean
        public Test6 test6() {
            return new Test6Impl();
        }
    }

    static class Test1Impl implements Test1 {

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2")
        @Override
        public void method1() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_2", routeKey);
        }
    }

    static class Test2Impl implements Test2 {

        private final Test2 proxy;

        public Test2Impl(Test2 proxy) {
            this.proxy = proxy;
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2")
        @Override
        public void method1() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_2", routeKey);

            proxy.method2();
            assertNull(DataSourceContextHolder.getRouteKey());
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2")
        @Override
        public void method2() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_2", routeKey);
        }
    }

    static class Test3Impl implements Test3 {

        private final Test3 proxy;

        public Test3Impl(Test3 proxy) {
            this.proxy = proxy;
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2")
        @Override
        public void method1() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_2", routeKey);

            proxy.method2();
            assertNotNull(DataSourceContextHolder.getRouteKey());
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2", routeContext = RouteContext.REQUIRED)
        @Override
        public void method2() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_2", routeKey);
        }
    }

    static class Test4Impl implements Test4 {

        private final Test4 proxy;

        public Test4Impl(Test4 proxy) {
            this.proxy = proxy;
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2")
        @Override
        public void method1() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_2", routeKey);

            proxy.method2();
            assertNull(DataSourceContextHolder.getRouteKey());
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_3", routeContext = RouteContext.REQUIRED)
        @Override
        public void method2() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNotNull(routeKey);
            assertEquals("ds_3", routeKey);
        }
    }

    static class Test5Impl implements Test5 {

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "ds_2", routeContext = RouteContext.REUSE)
        public void method1() {
            String routeKey = DataSourceContextHolder.getRouteKey();
            assertNull(routeKey);
        }
    }

    static class Test6Impl implements Test6 {

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "")
        @Override
        public void method1() {
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = " safdkjljkasfd")
        @Override
        public void method2() {
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "safdkjl jkasfd")
        @Override
        public void method3() {
        }

        @RoutingDataSourceAwareTransactional(dataSourceRouteKey = "safdkjl ")
        @Override
        public void method4() {
        }
    }
}