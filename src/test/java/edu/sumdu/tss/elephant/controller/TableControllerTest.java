package edu.sumdu.tss.elephant.controller;

import edu.sumdu.tss.elephant.helper.Keys;
import edu.sumdu.tss.elephant.helper.ViewHelper;
import edu.sumdu.tss.elephant.model.Database;
import edu.sumdu.tss.elephant.model.TableService;
import io.javalin.Javalin;
import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.sql2o.data.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TableControllerTest {
    @Test
    void shouldRegister() {
        // given
        Javalin app = mock(Javalin.class);
        TableController controller = new TableController(app);

        // when
        when(app.get(anyString(), any(), any())).thenReturn(app);
        controller.register(app);

        // then
        verify(app, atLeastOnce()).get(eq(TableController.BASIC_PAGE), any(), any());
        verify(app, atLeastOnce()).get(eq(TableController.BASIC_PAGE + "{table}"), any(), any());
    }

    @Test
    void shouldIndex() {
        // given
        Context context = mock(Context.class);
        Database database = mock(Database.class);
        Map<String, Object> model = new HashMap<>();
        List<String> breadcrumb = new ArrayList<>();
        Table table = mock(Table.class);

        // when
        when(context.sessionAttribute(Keys.DB_KEY)).thenReturn(database);
        when(context.sessionAttribute(Keys.MODEL_KEY)).thenReturn(model);
        when(database.getName()).thenReturn("dbname");

        try (MockedStatic<TableService> tableService = mockStatic(TableService.class);
             MockedStatic<ViewHelper> viewHelper = mockStatic(ViewHelper.class)) {
            tableService.when(() -> TableService.list("dbname")).thenReturn(table);
            viewHelper.when(() -> ViewHelper.breadcrumb(any())).thenReturn(breadcrumb);
            ViewHelper.defaultVariables(context);
            ViewHelper.cleanupSession(context);

            TableController.index(context);

            // then
            verify(context).render("/velocity/table/index.vm", model);
            assertTrue(breadcrumb.contains("Tables"));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldPreviewTable() {
        // given
        Context context = mock(Context.class);
        Database database = mock(Database.class);
        Validator<Integer> validator = mock(Validator.class);
        Map<String, Object> model = new HashMap<>();
        List<String> breadcrumb = new ArrayList<>();

        // when
        when(context.sessionAttribute(Keys.DB_KEY)).thenReturn(database);
        when(context.sessionAttribute(Keys.MODEL_KEY)).thenReturn(model);
        when(context.pathParam("table")).thenReturn("table");
        when(context.queryParamAsClass(any(), eq(Integer.class))).thenReturn(validator);
        when(validator.check(any(), anyString())).thenReturn(validator);
        when(validator.getOrDefault(anyInt())).thenReturn(10);
        when(database.getName()).thenReturn("database");

        try (MockedStatic<TableService> tableService = mockStatic(TableService.class);
             MockedStatic<ViewHelper> viewHelper = mockStatic(ViewHelper.class)) {
            tableService.when(() -> TableService.byName(anyString(), anyString(), anyInt(), anyInt())).thenReturn(null);
            tableService.when(() -> TableService.getTableSize(anyString(), anyString())).thenReturn(0);
            viewHelper.when(() -> ViewHelper.pager(anyInt(), anyInt())).thenReturn(null);
            viewHelper.when(() -> ViewHelper.breadcrumb(context)).thenReturn(breadcrumb);

            TableController.preview_table(context);

            // then
            verify(context).render("/velocity/table/show.vm", model);
        }
    }
}
