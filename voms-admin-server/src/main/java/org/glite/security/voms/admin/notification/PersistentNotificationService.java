/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.notification;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.Validate;
import org.glite.security.voms.admin.configuration.ServiceID;
import org.glite.security.voms.admin.notification.messages.VOMSNotification;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.generic.NotificationDAO;
import org.glite.security.voms.admin.persistence.model.notification.Notification;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum PersistentNotificationService implements NotificationServiceIF {

  INSTANCE;

  private static final Logger log = LoggerFactory
    .getLogger(PersistentNotificationService.class);

  private NotificationSettings notificationSettings;
  private NotificationDAO dao;

  private boolean notificationDisabled = false;

  private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
    1);

  private boolean dispatchingEnabled = true;

  private long dispatchingPeriodInSeconds = 5;

  private boolean started = false;

  private PersistentNotificationService() {

  }

  private Notification makeNotification(VOMSNotification n) {

    n.buildMessage();

    Notification persistentNotification = new Notification();

    persistentNotification.setMessageType(n.getClass()
      .getName());
    persistentNotification.setRecipients(n.getRecipientList());
    persistentNotification.setSubject(n.getSubject());
    persistentNotification.setCreationTime(new Date());
    persistentNotification.setMessage(n.getMessage());

    return persistentNotification;
  }

  private void persistNotification(Notification n) {

    Session s = HibernateFactory.getFactory()
      .openSession();
    dao.setSession(s);
    Transaction tx = s.beginTransaction();

    try {

      dao.makePersistent(n);
      log.debug("Persisting notification {}", n);

      tx.commit();

    } catch (Throwable e) {

      log.error(
        "Error persisting notification {}: {}. Rolling back transaction",
        new String[] { n.toString(), e.getMessage() }, e);

      try {
        tx.rollback();
      } catch (Throwable t) {
        log.error("Error rolling back transaction: {}", t.getMessage(), t);
      }

    } finally {

      try {
        s.close();
      } catch (Throwable e) {
        log.error("Error closing hibernate session: {}", e.getMessage(), e);
      }
    }

  }

  @Override
  public synchronized void send(VOMSNotification n) {

    Validate.isTrue(started, "Notification service was not initialized");
    Validate.notNull(n, "Notification n must not be null");

    preconditionsCheck();

    persistNotification(makeNotification(n));

  }

  public NotificationSettings getNotificationSettings() {

    return notificationSettings;
  }

  public void setNotificationSettings(
    NotificationSettings notificationSettings) {

    this.notificationSettings = notificationSettings;
  }

  public boolean isNotificationDisabled() {

    return notificationDisabled;
  }

  public void setNotificationDisabled(boolean notificationDisabled) {

    this.notificationDisabled = notificationDisabled;
  }

  public NotificationDAO getDao() {

    return dao;
  }

  public void setDao(NotificationDAO dao) {

    this.dao = dao;
  }

  @Override
  public List<Runnable> shutdownNow() {

    Validate.notNull(executorService);
    return executorService.shutdownNow();
  }

  protected void preconditionsCheck() {

    Validate.notNull(dao, "dao must not be null");
    Validate.notNull(notificationSettings,
      "notificationSettings must not be null");
    Validate.notNull(dao, "dao must not be null");
  }

  private void startDispatcher() {

    PersistentNotificationWorker worker = new PersistentNotificationWorker(dao,
      notificationSettings, ServiceID.getServiceID());

    log.info("Starting notification dispatcher with period: {} seconds",
      dispatchingPeriodInSeconds);

    executorService.scheduleAtFixedRate(worker, dispatchingPeriodInSeconds,
      dispatchingPeriodInSeconds, TimeUnit.SECONDS);
  }

  @Override
  public synchronized void start() {

    preconditionsCheck();
    if (dispatchingEnabled) {
      startDispatcher();
    }
    started = true;
  }
}
