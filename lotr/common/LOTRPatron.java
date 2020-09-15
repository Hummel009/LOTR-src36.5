/*
 * Decompiled with CFR 0.148.
 */
package lotr.common;

import java.util.ArrayList;
import java.util.UUID;

public class LOTRPatron {
    private static String[] shieldPledges = new String[]{"39f014d6-c028-4783-8c28-bef2b72b1cc7", "ef35a72a-ef00-4c2a-a2a9-58a54a7bb9fd", "c75394f3-6c1f-4efa-8568-2d02ece24ac9", "ae1b0559-b38b-4fd1-8c0c-7dc9afb8814b", "7e674e49-8a7e-4798-a6a1-5f16a8db6489", "db4bbf4c-5cd2-4929-95da-ce15256509d3", "5a72bd8f-83cf-43dc-a497-6ad48cf57443", "56097783-03b0-40a9-be4c-1746d2576cc8", "7d91a373-4b66-4d70-87fc-a4f37b0fa2ed", "ea900a96-99bb-4e5c-97e8-0a0caeef0a79", "8643b5d4-b0e8-4edc-beec-c83b2676a911", "b60c478a-01d5-4c7a-aaec-71de404585dc", "4f61d6e6-e688-49cd-9356-2319271d1bef", "22aa3a08-d8a5-4f13-a471-e29154a7eb83", "729dd43b-0ae7-4a27-acc4-1e66a55003ff", "a908bfd1-39f8-4735-8026-1526ac2e759c", "cc5b8640-0b8f-40a6-85d5-d7f51771c945", "d5da7dbe-5cdd-49c2-aa42-245358ba3f6c", "5ba78e5f-e705-4bda-ba27-4cfd837f0b3a", "6ba9ef8a-06db-4758-8da8-41f50036f028", "8e82512e-5190-485b-88a1-19d339b53aa4", "007045de-b231-464a-a0b5-1167eb4116a0", "038330a6-6622-4dbe-aeff-bab71bcab383", "48ab19b5-1e8a-49a9-ba73-226998bc697f", "0408aedd-1a75-411a-88e3-6f4704d8d546", "c8e6bf0c-495c-4b6d-93ff-753597d5a55b", "7f7423f5-01ef-4515-895d-4932b2770f02", "5f30c16d-bd8a-4018-8cb1-fd98da888dd9", "11540aec-b7b6-4f6f-a2c8-08a5e491d32b", "be36be1e-7317-4ff8-a0b9-f577a3e4ea63", "37b3bac5-96e5-463d-9a21-a2a398871ff1", "4c4664a2-302a-480a-8af7-115be9a77a6c", "8b0bf39a-2723-4253-9d71-4a9bfe46ac38", "b8bae7f0-c121-4d7a-8c68-4df8f93db2a5", "e69ddad2-db7a-48c5-aefe-20cc196d6ac0", "bc2f8edc-d1f9-4cd7-8018-d7e683d1cba9", "9f82367c-2c04-43a7-acf9-b8045a3368a9", "85d90c0f-5a4a-40f6-a13f-9bd60d4446b4", "f48a4426-54be-4674-b957-a43302285348", "40c8b50a-e2b6-48a9-b5e6-c6203fc6e531", "51010eca-bf43-4980-87ec-c21b4115efc9", "ab8341a2-308d-4900-bad3-af5b899f8831", "f0079443-90a2-42cc-a819-942db08343fe", "56c71aab-8a68-465d-b386-5f721dd68df6", "a470f15c-eb7a-460d-8db8-666abb6f3ff3", "519b7c6c-051e-47e9-9e20-8391333c388e", "9777aded-d971-4aab-9160-ec0e8ea31a0b", "e8b3f4c3-d283-4a3c-a480-7170131ce3e1", "c97f7ba6-a20e-497c-971a-5437ff60c596", "c0fe3f5c-955e-49fa-8676-0a2ace10da10", "060e68a0-60cd-4cfe-b0f3-6d4beefd1b2e"};
    private static String[] titlePledges = new String[]{"7bc56da6-f133-4e47-8d0f-a2776762bca6", "e48a5ae5-623b-413b-90fe-f32a215e2dd6", "9d8b2b0b-43c4-4abd-9d07-eee7e4dc2d19", "fee47fb3-bf03-475b-91ff-0f80a103a770", "db4bbf4c-5cd2-4929-95da-ce15256509d3", "7b3e16ef-717e-40d7-9889-d510e5f6dffe", "a519ce58-77b0-450b-8bb0-803857b9615c", "65c8a2ad-5913-48b4-9985-a335cadb35d4", "d2115ee1-77e8-46c9-a102-04501b41e321", "151a927c-7e61-47d0-9943-8ac0179e9b6e", "61896cbc-3618-4b1c-9fa1-35045e4c0610", "b2c2d67f-6d51-44fd-a0f3-b9b682e134b6", "00af0427-37cc-4433-9214-deffbb3285a1", "aea38a54-6a11-4b9b-b6db-35ecbcb440a7", "aa6fa877-d661-4443-8a73-d147b812989b", "2f873ef5-bc17-49f0-83b2-6ea3079ff960", "f6f2d9dc-2ba2-47c5-92cd-f9aa489125e2", "6c14992c-29ba-4891-9345-7577a0963f4c", "10fbd37c-224a-40da-bc78-13a0b85cec4a", "ce130f67-4e63-4da5-afb6-bc8ce374fa95", "b7c289e7-902c-47e2-8438-1eab0099af7c", "13231d21-71ef-47f3-8149-85360347a859", "0fa75dd8-980c-401f-88db-6ce31f1c35e4", "0e101e6e-da5c-4e4c-a5e9-96d3eb1c5ee3", "ab378b86-41f0-4503-8e36-32fcb18af7f9", "e4e02fb0-7547-4dd8-918a-1746ef2673d2", "5a72bd8f-83cf-43dc-a497-6ad48cf57443", "7b05d7fe-128d-473e-8ca3-08fa2c8f8a12", "ca621028-e4dc-4e95-b9b0-68376cf6db50", "729dd43b-0ae7-4a27-acc4-1e66a55003ff", "3042f8cf-57c7-44c7-bc5d-23f85ca269a5", "c6c244bd-9399-4411-a9f5-d82f3c17a19b", "2b53790e-5d01-4a94-9a40-71f4a32f8b71", "994121da-47a2-4c73-81b4-c0be7b267497", "ec7ead12-fe68-421e-aade-23088c091abc", "cb597740-bb91-41f0-bed8-46ded4156567", "e5c1162a-9509-4358-9caf-fb9bfe1d7c01", "188e4e9c-8c67-443d-9b6c-a351076a43e3", "b0b38d9e-f8b0-416d-aab1-540398883483", "05d987d8-7e3d-44b2-a170-234efe19f907", "8ee7b9d1-a94a-4692-ac22-437dbcfae915", "4e0f2008-bd3d-4ffc-b658-8cc47483152c", "2b430383-cbbc-47f6-9d4e-b442519a1818", "7445a287-ded9-4cfd-92c0-0b64b7be4c52", "7f4ab975-b73c-49b7-bff1-737dab63a70d", "a5a65891-9806-429e-814f-30b3b72702b9"};
    public static UUID elfBowPlayer = UUID.fromString("e48a5ae5-623b-413b-90fe-f32a215e2dd6");

    public static String[] getShieldPlayers() {
        ArrayList<String> list = new ArrayList<String>();
        for (String s : shieldPledges) {
            list.add(s);
        }
        for (String s : titlePledges) {
            list.add(s);
        }
        return list.toArray(new String[0]);
    }

    public static UUID[] getTitlePlayers() {
        ArrayList<UUID> list = new ArrayList<UUID>();
        for (String s : shieldPledges) {
            list.add(UUID.fromString(s));
        }
        for (String s : titlePledges) {
            list.add(UUID.fromString(s));
        }
        return list.toArray(new UUID[0]);
    }
}

