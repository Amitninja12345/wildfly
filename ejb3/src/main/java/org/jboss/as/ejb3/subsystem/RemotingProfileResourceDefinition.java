/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.ejb3.subsystem;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceRemoveStepHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.ejb3.remote.RemotingProfileService;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * {@link org.jboss.as.controller.ResourceDefinition} for remoting profiles.
 *
 * @author <a href="mailto:tadamski@redhat.com">Tomasz Adamski</a>
 */
public class RemotingProfileResourceDefinition extends SimpleResourceDefinition {

    public static final SimpleAttributeDefinition EXCLUDE_LOCAL_RECEIVER = new SimpleAttributeDefinitionBuilder(
            EJB3SubsystemModel.EXCLUDE_LOCAL_RECEIVER, ModelType.BOOLEAN, true).setAllowExpression(true)
            .setDefaultValue(ModelNode.FALSE).build();

    public static final SimpleAttributeDefinition LOCAL_RECEIVER_PASS_BY_VALUE = new SimpleAttributeDefinitionBuilder(
            EJB3SubsystemModel.LOCAL_RECEIVER_PASS_BY_VALUE, ModelType.BOOLEAN, true).setAllowExpression(true).build();

    private static final AttributeDefinition[] ATTRIBUTES = new AttributeDefinition[] { EXCLUDE_LOCAL_RECEIVER, LOCAL_RECEIVER_PASS_BY_VALUE, StaticEJBDiscoveryDefinition.INSTANCE };
    static final RemotingProfileAdd ADD_HANDLER = new RemotingProfileAdd(ATTRIBUTES);

    public static final RemotingProfileResourceDefinition INSTANCE = new RemotingProfileResourceDefinition();

    private RemotingProfileResourceDefinition() {
        super(PathElement.pathElement(EJB3SubsystemModel.REMOTING_PROFILE), EJB3Extension
                .getResourceDescriptionResolver(EJB3SubsystemModel.REMOTING_PROFILE), ADD_HANDLER, new ServiceRemoveStepHandler(RemotingProfileService.BASE_SERVICE_NAME, ADD_HANDLER));
    }

    @Override
    public void registerChildren(ManagementResourceRegistration subsystemRegistration) {
        subsystemRegistration.registerSubModel(RemotingEjbReceiverDefinition.INSTANCE);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        for (AttributeDefinition attr : ATTRIBUTES) {
            resourceRegistration.registerReadWriteAttribute(attr, null, new RemotingProfileResourceChildWriteAttributeHandler(attr));
        }
    }
}
