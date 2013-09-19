/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.facebook.api;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.util.List;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.social.NotAuthorizedException;

public class PlacesTemplateTest extends AbstractFacebookApiTest {

	@Test
	public void getCheckins() {
		mockServer.expect(requestTo("https://graph.facebook.com/me/posts?offset=0&limit=25&with=location"))
			.andExpect(method(GET))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("checkins"), MediaType.APPLICATION_JSON));
		List<Checkin> checkins = facebook.placesOperations().getCheckins();
		assertCheckins(checkins);
	}

	@Test
	public void getCheckins_withOffsetAndLimit() {
		mockServer.expect(requestTo("https://graph.facebook.com/me/posts?offset=50&limit=10&with=location"))
			.andExpect(method(GET))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("checkins"), MediaType.APPLICATION_JSON));
		List<Checkin> checkins = facebook.placesOperations().getCheckins(50, 10);
		assertCheckins(checkins);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getCheckins_unauthorized() {
		unauthorizedFacebook.placesOperations().getCheckins();
	}
	
	@Test
	public void getCheckins_forSpecificUser() {
		mockServer.expect(requestTo("https://graph.facebook.com/987654321/posts?offset=0&limit=25&with=location"))
			.andExpect(method(GET))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("checkins"), MediaType.APPLICATION_JSON));
		List<Checkin> checkins = facebook.placesOperations().getCheckins("987654321");
		assertCheckins(checkins);
	}

	@Test
	public void getCheckins_forSpecificUser_withOffsetAndLimit() {
		mockServer.expect(requestTo("https://graph.facebook.com/987654321/posts?offset=50&limit=10&with=location"))
			.andExpect(method(GET))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("checkins"), MediaType.APPLICATION_JSON));
		List<Checkin> checkins = facebook.placesOperations().getCheckins("987654321", 50, 10);
		assertCheckins(checkins);
	}

	@Test(expected = NotAuthorizedException.class)
	public void getCheckins_forSpecificUser_unauthorized() {
		unauthorizedFacebook.placesOperations().getCheckins("987654321");
	}

	@Test
	public void getCheckin() {
		mockServer.expect(requestTo("https://graph.facebook.com/10150431253050580"))
			.andExpect(method(GET))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("checkin"), MediaType.APPLICATION_JSON));
		Checkin checkin = facebook.placesOperations().getCheckin("10150431253050580");
		assertSingleCheckin(checkin);		
	}

	@Test(expected = NotAuthorizedException.class)
	public void getCheckin_unauthorized() {
		unauthorizedFacebook.placesOperations().getCheckin("987654321");
	}

	@Test
	public void checkin() {
		mockServer.expect(requestTo("https://graph.facebook.com/me/feed"))
			.andExpect(method(POST))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andExpect(content().string("place=123456789&coordinates=%7B%22latitude%22%3A%2232.943860253093%22%2C%22longitude%22%3A%22-96.648515652755%22%7D"))
			.andRespond(withSuccess("{\"id\":\"10150431253050580\"}", MediaType.APPLICATION_JSON));
		assertEquals("10150431253050580", facebook.placesOperations().checkin("123456789", 32.943860253093, -96.648515652755));
	}

	@Test(expected = NotAuthorizedException.class)
	public void checkin_unauthorized() {
		unauthorizedFacebook.placesOperations().checkin("123456789", 32.943860253093, -96.648515652755);
	}

	@Test
	public void checkin_withMessage() {
		mockServer.expect(requestTo("https://graph.facebook.com/me/feed"))
			.andExpect(method(POST))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andExpect(content().string("place=123456789&coordinates=%7B%22latitude%22%3A%2232.943860253093%22%2C%22longitude%22%3A%22-96.648515652755%22%7D&message=My+favorite+place"))
			.andRespond(withSuccess("{\"id\":\"10150431253050580\"}", MediaType.APPLICATION_JSON));
		assertEquals("10150431253050580", facebook.placesOperations().checkin("123456789", 32.943860253093, -96.648515652755, "My favorite place"));
	}

	@Test(expected = NotAuthorizedException.class)
	public void checkin_withMessage_unauthorized() {
		unauthorizedFacebook.placesOperations().checkin("123456789", 32.943860253093, -96.648515652755, "My favorite place");
	}

	@Test
	public void checkin_withMessageAndTags() {
		mockServer.expect(requestTo("https://graph.facebook.com/me/feed"))
			.andExpect(method(POST))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andExpect(content().string("place=123456789&coordinates=%7B%22latitude%22%3A%2232.943860253093%22%2C%22longitude%22%3A%22-96.648515652755%22%7D&message=My+favorite+place&tags=24680%2C13579"))
			.andRespond(withSuccess("{\"id\":\"10150431253050580\"}", MediaType.APPLICATION_JSON));
		assertEquals("10150431253050580", 
				facebook.placesOperations().checkin("123456789", 32.943860253093, -96.648515652755, "My favorite place", "24680", "13579"));
	}

	@Test(expected = NotAuthorizedException.class)
	public void checkin_withMessageAndTags_unauthorized() {
		unauthorizedFacebook.placesOperations().checkin("123456789", 32.943860253093, -96.648515652755, "My favorite place", "24680", "13579");
	}

	@Test
	public void search() {
		mockServer.expect(requestTo("https://graph.facebook.com/search?q=coffee&type=place&center=33.050278%2C-96.745833&distance=5280"))
			.andExpect(method(GET))
			.andExpect(header("Authorization", "OAuth someAccessToken"))
			.andRespond(withSuccess(jsonResource("places-list"), MediaType.APPLICATION_JSON));
		List<Page> places = facebook.placesOperations().search("coffee", 33.050278, -96.745833, 5280);
		assertEquals(2, places.size());
		assertEquals("117723491586638", places.get(0).getId());
		assertEquals("True Brew Coffee & Espresso Service", places.get(0).getName());
		assertEquals("Local business", places.get(0).getCategory());
		assertEquals("542 Haggard St", places.get(0).getLocation().getStreet());
		assertEquals("Plano", places.get(0).getLocation().getCity());
		assertEquals("TX", places.get(0).getLocation().getState());
		assertEquals("United States", places.get(0).getLocation().getCountry());
		assertEquals("75074-5529", places.get(0).getLocation().getZip());
		assertEquals(33.026239, places.get(0).getLocation().getLatitude(), 0.00001);
		assertEquals(-96.707089, places.get(0).getLocation().getLongitude(), 0.00001);
		assertEquals("169020919798274", places.get(1).getId());
		assertEquals("Starbucks Coffee", places.get(1).getName());
		assertEquals("Local business", places.get(1).getCategory());
		assertNull(places.get(1).getLocation().getStreet());
		assertEquals("Plano", places.get(1).getLocation().getCity());
		assertEquals("TX", places.get(1).getLocation().getState());
		assertEquals("United States", places.get(1).getLocation().getCountry());
		assertNull(places.get(1).getLocation().getZip());
		assertEquals(33.027734, places.get(1).getLocation().getLatitude(), 0.00001);
		assertEquals(-96.795133, places.get(1).getLocation().getLongitude(), 0.00001);		
	}

	@Test(expected = NotAuthorizedException.class)
	public void search_unauthorized() {
		unauthorizedFacebook.placesOperations().search("coffee", 33.050278, -96.745833, 5280);
	}

	private void assertSingleCheckin(Checkin checkin) {
		assertEquals("10150431253050580", checkin.getId());
		assertEquals("738140579", checkin.getFrom().getId());
		assertEquals("Craig Walls", checkin.getFrom().getName());
		Page place1 = checkin.getPlace();
		assertEquals("117372064948189", place1.getId());
		assertEquals("Freebirds World Burrito", place1.getName());
		assertEquals("238 W Campbell Rd", place1.getLocation().getStreet());
		assertEquals("Richardson", place1.getLocation().getCity());
		assertEquals("TX", place1.getLocation().getState());
		assertEquals("United States", place1.getLocation().getCountry());
		assertEquals("75080-3512", place1.getLocation().getZip());
		assertEquals(32.975537, place1.getLocation().getLatitude(), 0.0001);
		assertEquals(-96.722944, place1.getLocation().getLongitude(), 0.0001);
		assertEquals("6628568379", checkin.getApplication().getId());
		assertEquals("Facebook for iPhone", checkin.getApplication().getName());
		assertEquals(toDate("2011-03-13T01:00:49+0000"), checkin.getCreatedTime());
	}
	
	private void assertCheckins(List<Checkin> checkins) {
		assertEquals(2, checkins.size());
		assertSingleCheckin(checkins.get(0));
		Checkin checkin2 = checkins.get(1);
		assertEquals("10150140239512040", checkin2.getId());
		assertEquals("533477039", checkin2.getFrom().getId());
		assertEquals("Raymie Walls", checkin2.getFrom().getName());
		assertEquals(1, checkin2.getTags().size());
		assertEquals("738140579", checkin2.getTags().get(0).getId());
		assertEquals("Craig Walls", checkin2.getTags().get(0).getName());
		assertEquals("With my favorite people! ;-)", checkin2.getMessage());
		Page place2 = checkin2.getPlace();
		assertEquals("150366431753543", place2.getId());
		assertEquals("Somewhere", place2.getName());
		assertEquals(35.0231428, place2.getLocation().getLatitude(), 0.0001);
		assertEquals(-98.740305416667, place2.getLocation().getLongitude(), 0.0001);
		assertEquals("6628568379", checkin2.getApplication().getId());
		assertEquals("Facebook for iPhone", checkin2.getApplication().getName());
		assertEquals(toDate("2011-02-11T20:59:41+0000"), checkin2.getCreatedTime());
		assertEquals(2, checkin2.getLikes().size());
		assertEquals("1524405653", checkin2.getLikes().get(0).getId());
		assertEquals("Samuel Hugh Parsons", checkin2.getLikes().get(0).getName());
		assertEquals("1580082219", checkin2.getLikes().get(1).getId());
		assertEquals("Kris Len Nicholson", checkin2.getLikes().get(1).getName());
		assertEquals(1, checkin2.getComments().size());
		assertEquals("10150140239512040_15204657", checkin2.getComments().get(0).getId());
		assertEquals("100000094813002", checkin2.getComments().get(0).getFrom().getId());
		assertEquals("Otis Nelson", checkin2.getComments().get(0).getFrom().getName());
		assertEquals("You are not with me!!!!", checkin2.getComments().get(0).getMessage());
		assertEquals(toDate("2011-02-11T21:31:31+0000"), checkin2.getComments().get(0).getCreatedTime());
	}
	
}
