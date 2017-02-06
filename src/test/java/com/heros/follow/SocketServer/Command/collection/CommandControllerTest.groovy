package com.heros.follow.SocketServer.Command.collection

/**
 * Created by Show on 2017/2/1.
 */
class CommandControllerTest extends GroovyTestCase {
    void setUp() {
        super.setUp()
    }

    void tearDown() {
    }

    void testCall() {
        CommandController commandController = new CommandController()
        String res=commandController.call("710");
        res=res.substring(0,res.indexOf("]")+1);
        org.junit.Assert.assertEquals(res,"執行命令:查詢執行緒[710]")

        res=commandController.call("711");
        res=res.substring(0,res.indexOf("]")+1);
        org.junit.Assert.assertEquals(res,"執行命令:啟動執行緒[711]")

        res=commandController.call("999");
        res=res.substring(0,res.indexOf("]")+1);
        org.junit.Assert.assertEquals(res,"執行命令:錯誤或無命令[999]")
    }
}
