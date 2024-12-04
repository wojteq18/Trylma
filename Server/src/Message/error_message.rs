use super::message::message;

pub struct error_message
{
    pub code: u16,
    pub message: String,
}

impl message for error_message
{
    fn get_content(&self) -> String
    {
        return format!("{}: {}", self.code, self.message);
    }
}