package utilities.publisher;

import models.Bid;
import utilities.interfaces.IBidClient;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Publisher {

    private final Map<String, List<IPropertyListener>> propertyListeners;
    private String propertiesString;
    private final ExecutorService pool;
    private final int nrThreads;

    public Publisher() {
        this(new String[0]);
    }

    public Publisher(String[] properties) {
        this.nrThreads = 10;
        this.propertyListeners = Collections.synchronizedMap(new HashMap());
        this.propertyListeners.put((String)null, Collections.synchronizedList(new ArrayList()));
        String[] var2 = properties;
        int var3 = properties.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            this.propertyListeners.put(s, Collections.synchronizedList(new ArrayList()));
        }

        this.setPropertiesString();
        this.pool = Executors.newFixedThreadPool(10);
    }

    public void subscribeLocalListener(ILocalPropertyListener listener, String property) {
        this.subscribePropertyListener(listener, property);
    }

    public void subscribeRemoteListener(IRemotePropertyListener listener, String property) {
        this.subscribePropertyListener(listener, property);
    }

    private void subscribePropertyListener(IPropertyListener listener, String property) {
        this.checkInBehalfOfProgrammer(property);
        this.propertyListeners.get(property).add(listener);
    }

    public void unsubscribeLocalListener(ILocalPropertyListener listener, String property) {
        this.unsubscribeListener(listener, property);
    }

    public void unsubscribeRemoteListener(IRemotePropertyListener listener, String property) {
        this.unsubscribeListener(listener, property);
    }

    private void unsubscribeListener(IPropertyListener listener, String property) {
        if (property != null) {
            List listeners = this.propertyListeners.get(property);
            if (listeners != null) {
                listeners.remove(listener);
                this.propertyListeners.get(null).remove(listener);
            }
        } else {
            List<String> keyset = new ArrayList(this.propertyListeners.keySet());
            Iterator var4 = keyset.iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                this.propertyListeners.get(key).remove(listener);
            }
        }

    }

    public void inform(String property, Object oldValue, Object newValue) throws RemoteException {
        this.checkInBehalfOfProgrammer(property);
        List<IPropertyListener> listenersToBeInformed = new ArrayList();
        if (property != null) {
            listenersToBeInformed.addAll(this.propertyListeners.get(property));
            listenersToBeInformed.addAll(this.propertyListeners.get((Object)null));
        } else {
            List<String> keyset = new ArrayList(this.propertyListeners.keySet());
            Iterator var6 = keyset.iterator();

            while(var6.hasNext()) {
                String key = (String)var6.next();
                listenersToBeInformed.addAll(this.propertyListeners.get(key));
            }
        }

        Iterator var9 = listenersToBeInformed.iterator();

        while(var9.hasNext()) {
            IBidClient listener = (IBidClient)var9.next();
            Bid bid = (Bid)newValue;

            if (listener.getAuctionId() == bid.getAuctionId()) {
                PropertyChangeEvent event = new PropertyChangeEvent(this, property, oldValue, bid);
                InformListenerRunnable informListenerRunnable = new InformListenerRunnable((IPropertyListener) listener, event);
                this.pool.execute(informListenerRunnable);
            }
        }

    }

    public void registerProperty(String property) {
        if (property.equals("")) {
            throw new RuntimeException("a property cannot be an empty string");
        } else {
            if (!this.propertyListeners.containsKey(property)) {
                this.propertyListeners.put(property, Collections.synchronizedList(new ArrayList()));
                this.setPropertiesString();
            }

        }
    }

    public void unregisterProperty(String property) {
        this.checkInBehalfOfProgrammer(property);
        if (property != null) {
            this.propertyListeners.remove(property);
        } else {
            List<String> keyset = new ArrayList(this.propertyListeners.keySet());
            Iterator var3 = keyset.iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                if (key != null) {
                    this.propertyListeners.remove(key);
                }
            }
        }

        this.setPropertiesString();
    }

    private void setPropertiesString() {
        List<String> properties = this.getProperties();
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        boolean firstProperty = true;

        String property;
        for(Iterator var4 = properties.iterator(); var4.hasNext(); sb.append(property)) {
            property = (String)var4.next();
            if (firstProperty) {
                firstProperty = false;
            } else {
                sb.append(", ");
            }
        }

        sb.append(" }");
        this.propertiesString = sb.toString();
    }

    private void checkInBehalfOfProgrammer(String property) {
        if (!this.propertyListeners.containsKey(property)) {
            throw new RuntimeException("property " + property + " is not a published property, please make a choice out of: " + this.propertiesString);
        }
    }

    public List<String> getProperties() {
        List<String> properties = new ArrayList(this.propertyListeners.keySet());
        return Collections.unmodifiableList(properties);
    }

    private class InformListenerRunnable implements Runnable {
        IPropertyListener listener;
        PropertyChangeEvent event;

        public InformListenerRunnable(IPropertyListener listener, PropertyChangeEvent event) {
            this.listener = listener;
            this.event = event;
        }

        public void run() {
            if (this.listener instanceof ILocalPropertyListener) {
                ILocalPropertyListener localListener = (ILocalPropertyListener)this.listener;
                localListener.propertyChange(this.event);
            } else {
                IRemotePropertyListener remoteListener = (IRemotePropertyListener)this.listener;

                try {
                    remoteListener.propertyChange(this.event);
                } catch (RemoteException var3) {
                    Publisher.this.unsubscribeListener(this.listener, (String)null);
                    Logger.getLogger(Publisher.class.getName()).log(Level.SEVERE, (String)null, var3);
                }
            }

        }
    }
}
