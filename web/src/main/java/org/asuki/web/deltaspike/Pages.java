package org.asuki.web.deltaspike;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jsf.api.config.view.View;
import org.apache.deltaspike.jsf.api.config.view.View.Extension;
import org.apache.deltaspike.jsf.api.config.view.View.NavigationMode;
import org.apache.deltaspike.jsf.api.config.view.View.ViewParameterMode;

@View(extension = Extension.XHTML, navigation = NavigationMode.REDIRECT)
public interface Pages extends ViewConfig {

    public interface Access extends Pages {

        /* @ViewAccessScoped */

        @View(name = "bus_line")
        public class BusLine implements Pages {
        }

        public class Date implements Pages {
        }

        public class Seat implements Pages {
        }

        public class Overview implements Pages {
        }
    }

    public interface Group extends Pages {

        @View(name = "single_page")
        public class SinglePage implements Group {
        }
    }

    @View(viewParams = ViewParameterMode.INCLUDE)
    public class Ordered implements Pages {
    };

    public class Orders implements Pages {
    };

}
