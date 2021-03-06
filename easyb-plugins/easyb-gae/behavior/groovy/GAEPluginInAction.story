import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.labs.taskqueue.TaskOptions
import com.google.appengine.api.labs.taskqueue.dev.QueueStateInfo

using "GAE"


scenario "Creating a simple entity easily", {
  given "a ds instance", {
    ds = datastore
  }
  then "it should be found and contain all relevant datapoints", {
    ds.prepare(new Query("yam")).countEntities().shouldBe 0
    ds.put(new Entity("yam"));
    ds.put(new Entity("yam"));
    ds.prepare(new Query("yam")).countEntities().shouldBe 2
  }
}


scenario "Using memcache should work", {
  given "a memcache instance", {
    ms = memcache
  }
  then "it should support storing information", {
    ms.contains("yar").shouldBe false
    ms.put("yar", "foo")
    ms.contains("yar").shouldBe true
  }
}


scenario "Using user service should work", {
  given "a user service instance", {
    userService = user
  }
  and "everything has been set up properly", {
    localServiceHelper.setEnvIsAdmin(true)
    localServiceHelper.setEnvIsLoggedIn(true)
  }
  then "it should be admin", {
    userService.isUserAdmin().shouldBe true
  }
}


scenario "Using task service should work", {
  given "a task", {
    queue.add(TaskOptions.Builder.taskName("task29"))
  }
  then "it should be in the queue", {
    Thread.sleep(1000)
    QueueStateInfo qsi = localTaskQueue.getQueueStateInfo().get(queue.getQueueName())
    qsi.getTaskInfo().size().shouldBe 1
    qsi.getTaskInfo().get(0).getTaskName().shouldBe "task29"
  }
}

