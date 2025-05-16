package com.csc.java.ai.langchain4j;


import com.csc.java.ai.langchain4j.bean.ChatMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest
public class MongoCrudTest {

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Test
//    public void testInsert() {
//
//        mongoTemplate.insert(new ChatMessages(1L, "who am i"));
//    }

    @Test
    public void testInsert2() {

        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setContent("who am i 2");
        mongoTemplate.insert(chatMessages);
    }

    @Test
    public void testFindById() {
        ChatMessages byId = mongoTemplate.findById(1L, ChatMessages.class);
        System.out.println(byId);
    }

    @Test
    public void testUpdate() {

        Criteria id = Criteria.where("_id").is("100");
        Query query = new Query(id);
        Update update = new Update();
        update.set("content", "new ui");
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    @Test
    public void testDelete() {
        Criteria id = Criteria.where("_id").is("100");
        Query query = new Query(id);
        mongoTemplate.remove(query, ChatMessages.class);
    }
}
