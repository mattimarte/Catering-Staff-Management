package catering.businesslogic.sheet;

import catering.businesslogic.event.Service;
import catering.businesslogic.user.User;

public abstract class SummarySheet {
    protected int id;
    protected Service service;
    protected User owner;

    protected SummarySheet(Service service, User owner) {
        this.service = service;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public Service getService() {
        return service;
    }

    public boolean isOwner(User user) {
        return owner.equals(user);
    }


}
