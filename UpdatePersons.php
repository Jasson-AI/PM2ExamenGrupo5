<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: PUT");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

include_once 'conexion.php';
include_once 'Personas.php';

$db = new DataBase();
$instant = $db->getConnection();

$pinst = new Personas($instant);
$data = json_decode(file_get_contents("php://input"));

if (
    isset($data) &&
    !empty($data->id) &&
    !empty($data->nombres) &&
    !empty($data->longitud) &&
    !empty($data->latitud)
) {
    $pinst->id = $data->id;
    $pinst->nombres = $data->nombres ?? '';
    $pinst->telefono = $data->telefono ?? '';
    $pinst->latitud = $data->latitud ?? '';
    $pinst->longitud = $data->longitud ?? '';

    if ($pinst->updatePerson()) {
        http_response_code(200);
        echo json_encode([
            "issuccess" => true,
            "message" => "Registro actualizado correctamente"
        ]);
    } else {
        http_response_code(503);
        echo json_encode([
            "issuccess" => false,
            "message" => "No se pudo actualizar el registro"
        ]);
    }
} else {
    http_response_code(400);
    echo json_encode([
        "issuccess" => false,
        "message" => "Datos incompletos"
    ]);
}
?>